package com.example.offlinepos.Sync

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.offlinepos.data.local.AppDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.security.MessageDigest

class BackupManager(
    private val db: AppDatabase,
    private val now: () -> Long,
) {
    companion object {
        // All workers and manual Sync Now calls share one process-wide snapshot lock.
        private val snapshotMutex = Mutex()
    }

    suspend fun quiesce() = snapshotMutex.withLock {
        configureConnection()
        db.openHelper.writableDatabase.query(SimpleSQLiteQuery("PRAGMA wal_checkpoint(PASSIVE)"))
            .use { it.moveToFirst() }
    }

    suspend fun snapshot(target: File) {
        require(target.parentFile?.exists() == true)
        snapshotMutex.withLock {
            configureConnection()
            var lastError: Throwable? = null
            repeat(5) { attempt ->
                val temp = File(target.parentFile, ".${target.name}.tmp.$attempt")
                temp.delete()
                try {
                    // VACUUM INTO is a SQLite command, not a SELECT query. Android's
                    // SupportSQLiteDatabase.query() rejects it with code 0 / SQLITE_OK.
                    // Quote the controlled cache path and execute it through execSQL.
                    val escapedPath = temp.absolutePath.replace("'", "''")
                    db.openHelper.writableDatabase.execSQL("VACUUM INTO '$escapedPath'")
                    check(temp.renameTo(target)) { "Unable to finalize database snapshot" }
                    return
                } catch (error: Throwable) {
                    lastError = error
                    temp.delete()
                    if (attempt < 4) delay(300L * (1L shl attempt))
                }
            }
            throw lastError ?: IllegalStateException("Database snapshot failed")
        }
    }

    private fun configureConnection() {
        // PRAGMA returns a result row on Android; execSQL() rejects it as a query.
        db.openHelper.writableDatabase
            .query(SimpleSQLiteQuery("PRAGMA busy_timeout=30000"))
            .use { it.moveToFirst() }
    }

    fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(64 * 1024)
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    fun manifest(snapshot: File, appVersion: String, schemaVersion: Int, deviceId: String): String = """
{
"format": "offlinepos-backup",
"formatVersion": 1,
"createdAtEpochMs": ${now()},
"appVersion": "$appVersion",
"schemaVersion": $schemaVersion,
"deviceId": "$deviceId",
"snapshotSha256": "${sha256(snapshot)}",
"snapshotBytes": ${snapshot.length()}
}
""".trimIndent()
}
