package com.example.offlinepos.Sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import com.example.offlinepos.data.local.AppDatabase
import com.example.offlinepos.data.local.ErrorClass
import com.example.offlinepos.data.local.SyncState
import com.example.offlinepos.server.StaffClientApi
import com.example.offlinepos.ui.StoreConfig

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    private val db = AppDatabase.get(context)
    private val storeConfig = StoreConfig(context)

    override suspend fun doWork(): Result {
        val now = System.currentTimeMillis()
        val pending = db.outboxDao().loadReady(now)
        if (pending.isEmpty()) return Result.success()
        val baseUrl = storeConfig.getLastConnectedServerUrl().trim()
        if (baseUrl.isBlank()) return Result.retry()

        var shouldRetry = false
        pending.forEach { operation ->
            val result = StaffClientApi.pushOutboxOperation(baseUrl, operation.operationId, operation.payloadJson)
            if (result.isSuccess) {
                db.outboxDao().markResult(operation.operationId, SyncState.ACKED, null, null, operation.retryCount, Long.MAX_VALUE, now)
            } else {
                val retryCount = operation.retryCount + 1
                db.outboxDao().markResult(
                    operation.operationId,
                    SyncState.RETRYABLE_FAILURE,
                    ErrorClass.NETWORK,
                    result.exceptionOrNull()?.message ?: "Network error",
                    retryCount,
                    now + nextAttemptDelayMs(retryCount),
                    now,
                )
                shouldRetry = true
            }
        }
        return if (shouldRetry) Result.retry() else Result.success()
    }

    companion object {
        fun constraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val backoffPolicy: BackoffPolicy = BackoffPolicy.EXPONENTIAL
    }
}
