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
    private val snapshotMutex = Mutex()

    suspend fun quiesce() = snapshotMutex.withLock {
        db.openHelper.writableDatabase.execSQL("PRAGMA busy_timeout=30000")
        db.openHelper.writableDatabase.query(SimpleSQLiteQuery("PRAGMA wal_checkpoint(PASSIVE)"))
            .use { it.moveToFirst() }
    }
    suspend fun snapshot(target: File) {
        require(target.parentFile?.exists() == true)
        snapshotMutex.withLock {
            db.openHelper.writableDatabase.execSQL("PRAGMA busy_timeout=30000")
            var lastError: Throwable? = null
            repeat(4) { attempt ->
                val temp = File(target.parentFile, ".${target.name}.tmp.$attempt")
                temp.delete()
                try {
                    db.openHelper.writableDatabase
                        .query(SimpleSQLiteQuery("VACUUM INTO ?", arrayOf(temp.absolutePath)))
                        .use { it.moveToFirst() }
                    if (!temp.renameTo(target)) error("Unable to finalize database snapshot")
                    return
                } catch (t: Throwable) {
                    lastError = t
                    temp.delete()
                    if (attempt < 3) delay(250L * (1L shl attempt))
                }
            }
            throw lastError ?: IllegalStateException("Database snapshot failed")
        }
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
    fun manifest(
        snapshot: File,
        appVersion: String,
        schemaVersion: Int,
        deviceId: String,
    ): String = """
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
