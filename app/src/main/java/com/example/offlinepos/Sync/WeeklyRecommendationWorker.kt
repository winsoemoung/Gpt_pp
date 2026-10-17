package com.example.offlinepos.Sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.offlinepos.R
import com.example.offlinepos.ai.AiPosEngine
import com.example.offlinepos.ai.PosDatabaseContext
import com.example.offlinepos.data.local.AppDatabase
import com.example.offlinepos.ui.StoreConfig
import kotlinx.coroutines.flow.first
import java.util.Locale

class WeeklyRecommendationWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val db = AppDatabase.get(applicationContext)
            val products = db.productDao().observeActive().first()
            val sales = db.checkoutDao().observeSales().first()
            val items = db.checkoutDao().observeAllSaleItems().first()
            val customers = db.customerDao().observeAllCustomers().first()
            val profile = StoreConfig(applicationContext).getProfile()
            val saleById = sales.associateBy { it.saleId }
            val last7Days = System.currentTimeMillis() - 7L * 24L * 60L * 60L * 1000L
            val weeklyItems = items.filter { item ->
                val sale = saleById[item.saleId]
                sale != null && sale.createdAtEpochMs >= last7Days && sale.status != "VOIDED"
            }
            val soldByProduct = weeklyItems.groupBy { it.productId }.mapValues { (_, rows) ->
                rows.sumOf { (it.quantity - it.returnedQuantity).coerceAtLeast(0L) }
            }
            val candidates = products.mapNotNull { product ->
                val sold = soldByProduct[product.productId] ?: 0L
                val daily = sold.toDouble() / 7.0
                val forecast90 = kotlin.math.ceil(daily * 90.0).toLong()
                val coverage = if (daily > 0.0) (product.stockQuantity / daily).toLong() else Long.MAX_VALUE
                if (sold > 0L || product.stockQuantity <= product.lowStockThreshold) {
                    "${product.name}: sold7=$sold, stock=${product.stockQuantity}, coverageDays=${if (coverage == Long.MAX_VALUE) "infinite" else coverage}, forecast90=$forecast90, reorder=${(forecast90 * 1.2 - product.stockQuantity).coerceAtLeast(0.0).toLong()}"
                } else null
            }.sortedByDescending { it.contains("coverageDays=", ignoreCase = true) }.take(20)
            val debtors = customers.filter { it.currentDebtMinor > 0L }
            val lowStock = products.filter { it.stockQuantity <= it.lowStockThreshold }
            val context = PosDatabaseContext(
                products = products,
                sales = sales,
                customers = customers,
                categories = products.map { it.category }.distinct(),
                storeProfile = profile,
                totalRevenue = sales.sumOf { it.totalMinor },
                todayRevenue = 0L,
                todaySalesCount = 0,
                lowStockCount = lowStock.size,
                lowStockProducts = lowStock,
                debtors = debtors,
                totalDebtAmount = debtors.sumOf { it.currentDebtMinor },
            )
            val prompt = buildString {
                append("Weekly stock recommendation. Explain in Burmese. Analysis only; do not execute any action. Prioritize products that may run out within 90 days.\n")
                candidates.forEach { appendLine(it) }
            }
            val (aiText, _) = AiPosEngine(applicationContext).processUserCommand(prompt, context)
            showNotification(profile.storeName, aiText.take(220))
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }

    private fun showNotification(storeName: String, text: String) {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "weekly_stock_recommendations"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel(channelId, "Weekly Stock Recommendations", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_agenda)
            .setContentTitle("$storeName — Weekly Stock Recommendation")
            .setContentText(text.replace("\n", " "))
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .build()
        manager.notify(7875, notification)
    }
}
