package com.example.offlinepos.Sync

import android.accounts.Account
import android.content.Context
import com.example.offlinepos.data.local.AppDatabase
import com.google.android.gms.auth.GoogleAuthUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class GoogleDriveSyncManager(private val context: Context) {
    companion object {
        private const val DRIVE_SCOPE = "oauth2:https://www.googleapis.com/auth/drive.appdata"
        private const val BACKUP_NAME = "offline_pos_backup.db"
    }

    suspend fun uploadLatestBackup(accountEmail: String): Result<Long> = withContext(Dispatchers.IO) {
        runCatching {
            val token = GoogleAuthUtil.getToken(context, Account(accountEmail, "com.google"), DRIVE_SCOPE)
            val db = AppDatabase.get(context)
            val backupManager = BackupManager(db) { System.currentTimeMillis() }
            val file = File(context.cacheDir, BACKUP_NAME)
            backupManager.quiesce()
            backupManager.snapshot(file)
            val existingId = findBackupId(token)
            val responseCode = if (existingId == null) {
                createBackup(token, file)
            } else {
                updateBackup(token, existingId, file)
            }
            if (responseCode !in 200..299) error("Google Drive upload failed: HTTP $responseCode")
            file.length()
        }
    }

    private fun findBackupId(token: String): String? {
        val url = URL("https://www.googleapis.com/drive/v3/files?q='appDataFolder'%20in%20parents%20and%20name='offline_pos_backup.db'%20and%20trashed=false&spaces=appDataFolder&fields=files(id)")
        val connection = open(url, token, "GET")
        return try {
            if (connection.responseCode !in 200..299) null
            else {
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                JSONObject(body).optJSONArray("files")?.optJSONObject(0)?.optString("id")?.takeIf { !it.isNullOrBlank() }
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun createBackup(token: String, file: File): Int {
        val boundary = "offlinepos_${System.currentTimeMillis()}"
        val connection = open(URL("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart"), token, "POST")
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "multipart/related; boundary=$boundary")
        val metadata = JSONObject().put("name", BACKUP_NAME).put("parents", org.json.JSONArray().put("appDataFolder"))
        connection.outputStream.use { output ->
            output.write("--$boundary\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n".toByteArray())
            output.write(metadata.toString().toByteArray())
            output.write("\r\n--$boundary\r\nContent-Type: application/octet-stream\r\n\r\n".toByteArray())
            file.inputStream().use { it.copyTo(output) }
            output.write("\r\n--$boundary--\r\n".toByteArray())
        }
        return connection.responseCode.also { connection.disconnect() }
    }

    private fun updateBackup(token: String, fileId: String, file: File): Int {
        val connection = open(URL("https://www.googleapis.com/upload/drive/v3/files/$fileId?uploadType=media"), token, "PATCH")
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/octet-stream")
        file.inputStream().use { input -> connection.outputStream.use { output -> input.copyTo(output) } }
        return connection.responseCode.also { connection.disconnect() }
    }

    private fun open(url: URL, token: String, method: String): HttpURLConnection {
        return (url.openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 20_000
            readTimeout = 60_000
            setRequestProperty("Authorization", "Bearer $token")
            setRequestProperty("Accept", "application/json")
        }
    }
}
