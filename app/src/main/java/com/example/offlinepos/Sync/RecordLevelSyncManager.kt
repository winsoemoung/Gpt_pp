package com.example.offlinepos.Sync

import android.content.Context
import android.database.Cursor
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.offlinepos.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** Two-device, offline-first record merge: SKU identity for products and LWW for conflicts. */
class RecordLevelSyncManager(private val context: Context) {
    companion object {
        private const val SCOPE = "oauth2:https://www.googleapis.com/auth/drive.appdata"
        private const val FILE_NAME = "offline_pos_records_v2.json"
        private const val DEVICE_ID = "SAN-THIT-POS-01"
        private val TABLES = listOf(
            "products", "sales", "sale_items", "payments", "customers", "debt_transactions",
            "purchases", "shifts", "parked_orders", "stock_movements", "return_transactions",
            "audit_events", "sync_outbox", "sync_tombstones",
        )
    }

    suspend fun sync(accountEmail: String): Result<Int> = withContext(Dispatchers.IO) {
        runCatching {
            val token = com.google.android.gms.auth.GoogleAuthUtil.getToken(
                context, android.accounts.Account(accountEmail, "com.google"), SCOPE,
            )
            val db = AppDatabase.get(context)
            val local = readBundle(db)
            val remote = findFileId(token)?.let { download(token, it) }
            val merged = mergeBundles(local, remote)
            applyBundle(db, merged)
            val file = File(context.cacheDir, FILE_NAME)
            file.writeText(merged.toString())
            val id = findFileId(token)
            val code = if (id == null) create(token, file) else update(token, id, file)
            if (code !in 200..299) error("Record merge upload failed: HTTP $code")
            context.getSharedPreferences("pos_sync_status", Context.MODE_PRIVATE).edit()
                .putLong("last_record_sync_ms", System.currentTimeMillis())
                .putInt("last_record_count", countRows(merged))
                .apply()
            countRows(merged)
        }
    }

    private fun countRows(bundle: JSONObject): Int {
        var count = 0
        bundle.optJSONObject("tables")?.keys()?.forEach { count += bundle.optJSONObject("tables")?.optJSONArray(it)?.length() ?: 0 }
        return count
    }

    private fun readBundle(db: AppDatabase): JSONObject {
        val tables = JSONObject()
        val sqlite = db.openHelper.readableDatabase
        TABLES.forEach { table ->
            val rows = JSONArray()
            try {
                sqlite.query(SimpleSQLiteQuery("SELECT * FROM $table")).use { cursor ->
                    val columns = cursor.columnNames
                    while (cursor.moveToNext()) {
                        val row = JSONObject()
                        columns.forEachIndexed { index, column -> row.put(column, cursorValue(cursor, index)) }
                        rows.put(row)
                    }
                }
            } catch (_: Exception) { }
            tables.put(table, rows)
        }
        return JSONObject()
            .put("format", "offlinepos-records-v2")
            .put("deviceId", DEVICE_ID)
            .put("conflictPolicy", "LAST_WRITE_WINS")
            .put("productIdentity", "SKU")
            .put("tables", tables)
    }

    private fun cursorValue(cursor: Cursor, index: Int): Any = when (cursor.getType(index)) {
        Cursor.FIELD_TYPE_NULL -> JSONObject.NULL
        Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(index)
        Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(index)
        Cursor.FIELD_TYPE_BLOB -> android.util.Base64.encodeToString(cursor.getBlob(index), android.util.Base64.NO_WRAP)
        else -> cursor.getString(index)
    }

    private fun mergeBundles(local: JSONObject, remote: JSONObject?): JSONObject {
        if (remote == null) return local
        val out = JSONObject().put("format", "offlinepos-records-v2")
            .put("deviceId", DEVICE_ID).put("conflictPolicy", "LAST_WRITE_WINS").put("productIdentity", "SKU")
        val outputTables = JSONObject()
        val localTables = local.optJSONObject("tables") ?: JSONObject()
        val remoteTables = remote.optJSONObject("tables") ?: JSONObject()
        TABLES.forEach { table ->
            val merged = linkedMapOf<String, JSONObject>()
            listOf(localTables.optJSONArray(table), remoteTables.optJSONArray(table)).forEach { rows ->
                if (rows == null) return@forEach
                for (i in 0 until rows.length()) {
                    val row = rows.optJSONObject(i) ?: continue
                    val key = rowKey(table, row)
                    val previous = merged[key]
                    if (previous == null || isNewer(row, previous)) merged[key] = row
                }
            }
            val array = JSONArray()
            merged.values.sortedBy { rowKey(table, it) }.forEach { array.put(it) }
            outputTables.put(table, array)
        }
        return out.put("tables", outputTables)
    }

    private fun rowKey(table: String, row: JSONObject): String {
        if (table == "products") return "sku:${row.optString("sku").trim().lowercase()}"
        val candidates = listOf("tombstoneId", "saleId", "lineId", "paymentId", "customerId", "txId", "purchaseId", "shiftId", "parkId", "movementId", "returnId", "eventId", "operationId", "productId")
        val key = candidates.firstOrNull { row.has(it) } ?: "_row"
        return "$key:${row.opt(key).toString()}"
    }

    private fun isNewer(candidate: JSONObject, current: JSONObject): Boolean {
        val a = candidate.optLong("deletedAtEpochMs", candidate.optLong("updatedAtEpochMs", candidate.optLong("createdAtEpochMs", 0L)))
        val b = current.optLong("deletedAtEpochMs", current.optLong("updatedAtEpochMs", current.optLong("createdAtEpochMs", 0L)))
        return a > b || (a == b && candidate.toString() > current.toString())
    }

    private fun applyBundle(db: AppDatabase, bundle: JSONObject) {
        val sqlite = db.openHelper.writableDatabase
        val tables = bundle.optJSONObject("tables") ?: return
        sqlite.beginTransaction()
        try {
            listOf("products", "customers", "sales", "purchases", "shifts", "parked_orders", "sale_items", "payments", "debt_transactions", "stock_movements", "return_transactions", "audit_events", "sync_outbox", "sync_tombstones").forEach { table ->
                val rows = tables.optJSONArray(table) ?: return@forEach
                val columns = tableColumns(sqlite, table)
                for (i in 0 until rows.length()) {
                    val row = rows.optJSONObject(i) ?: continue
                    val names = columns.filter { row.has(it) }
                    if (names.isEmpty()) continue
                    val args = names.map { valueForSql(row.opt(it)) }.toTypedArray()
                    try { sqlite.execSQL("INSERT OR REPLACE INTO $table (${names.joinToString(",")}) VALUES (${names.joinToString(",") { "?" }})", args) } catch (_: Exception) { }
                }
            }
            val tombstones = tables.optJSONArray("sync_tombstones") ?: JSONArray()
            for (i in 0 until tombstones.length()) {
                val t = tombstones.optJSONObject(i) ?: continue
                when (t.optString("entityType")) {
                    "customers" -> sqlite.execSQL("DELETE FROM customers WHERE customerId = ?", arrayOf(t.optString("entityId")))
                    "parked_orders" -> sqlite.execSQL("DELETE FROM parked_orders WHERE parkId = ?", arrayOf(t.optString("entityId")))
                    "products" -> sqlite.execSQL("UPDATE products SET isActive = 0, updatedAtEpochMs = ? WHERE sku = ?", arrayOf(t.optLong("deletedAtEpochMs"), t.optString("entityId")))
                }
            }
            sqlite.setTransactionSuccessful()
        } finally { sqlite.endTransaction() }
    }

    fun recordTombstone(db: AppDatabase, entityType: String, entityId: String) {
        val now = System.currentTimeMillis()
        db.openHelper.writableDatabase.execSQL(
            "INSERT OR REPLACE INTO sync_tombstones(tombstoneId,entityType,entityId,deletedAtEpochMs,deletedByDeviceId) VALUES(?,?,?,?,?)",
            arrayOf("$entityType:$entityId", entityType, entityId, now, DEVICE_ID),
        )
    }

    private fun tableColumns(db: androidx.sqlite.db.SupportSQLiteDatabase, table: String): List<String> {
        val result = mutableListOf<String>()
        db.query(SimpleSQLiteQuery("PRAGMA table_info($table)")).use { c ->
            val index = c.getColumnIndex("name")
            while (c.moveToNext()) if (index >= 0) result += c.getString(index)
        }
        return result
    }
    private fun valueForSql(value: Any?): Any? = if (value == null || value == JSONObject.NULL) null else value
    private fun findFileId(token: String): String? {
        val c = open(URL("https://www.googleapis.com/drive/v3/files?q='appDataFolder'%20in%20parents%20and%20name='$FILE_NAME'%20and%20trashed=false&spaces=appDataFolder&fields=files(id)"), token, "GET")
        return try { if (c.responseCode !in 200..299) null else JSONObject(c.inputStream.bufferedReader().use { it.readText() }).optJSONArray("files")?.optJSONObject(0)?.optString("id") } finally { c.disconnect() }
    }
    private fun download(token: String, id: String) = JSONObject(open(URL("https://www.googleapis.com/drive/v3/files/$id?alt=media"), token, "GET").inputStream.bufferedReader().use { it.readText() })
    private fun create(token: String, file: File): Int {
        val boundary = "offlinepos_records_${System.currentTimeMillis()}"
        val c = open(URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart"), token, "POST")
        c.doOutput = true; c.setRequestProperty("Content-Type", "multipart/related; boundary=$boundary")
        val meta = JSONObject().put("name", FILE_NAME).put("parents", JSONArray().put("appDataFolder"))
        c.outputStream.use { out -> out.write("--$boundary\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n$meta\r\n--$boundary\r\nContent-Type: application/json\r\n\r\n".toByteArray()); file.inputStream().use { it.copyTo(out) }; out.write("\r\n--$boundary--\r\n".toByteArray()) }
        return c.responseCode.also { c.disconnect() }
    }
    private fun update(token: String, id: String, file: File): Int {
        val c = open(URL("https://www.googleapis.com/upload/drive/v3/files/$id?uploadType=media"), token, "PATCH")
        c.doOutput = true; c.setRequestProperty("Content-Type", "application/json")
        file.inputStream().use { input -> c.outputStream.use { output -> input.copyTo(output) } }
        return c.responseCode.also { c.disconnect() }
    }
    private fun open(url: URL, token: String, method: String) = (url.openConnection() as HttpURLConnection).apply { requestMethod = method; connectTimeout = 20_000; readTimeout = 60_000; setRequestProperty("Authorization", "Bearer $token"); setRequestProperty("Accept", "application/json") }
}
