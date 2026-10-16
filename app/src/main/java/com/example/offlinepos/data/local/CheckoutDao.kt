package com.example.offlinepos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
abstract class CheckoutDao {
    @Query("SELECT * FROM sales ORDER BY createdAtEpochMs DESC")
    abstract fun observeSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    abstract suspend fun getItemsForSale(saleId: String): List<SaleItemEntity>

    @Query("SELECT * FROM sale_items")
    abstract fun observeAllSaleItems(): Flow<List<SaleItemEntity>>

    @Query("SELECT status FROM sales WHERE saleId = :saleId LIMIT 1")
    abstract suspend fun getSaleStatus(saleId: String): String?

    @Query("SELECT * FROM payments WHERE saleId = :saleId LIMIT 1")
    abstract suspend fun getPaymentForSale(saleId: String): PaymentEntity?

    @Query("SELECT * FROM audit_events ORDER BY createdAtEpochMs DESC LIMIT 50")
    abstract fun observeAuditEvents(): Flow<List<AuditEventEntity>>

    @Insert
    abstract suspend fun insertSale(sale: SaleEntity)

    @Insert
    abstract suspend fun insertItems(items: List<SaleItemEntity>)

    @Insert
    abstract suspend fun insertPayment(payment: PaymentEntity)

    @Insert
    abstract suspend fun insertAudit(event: AuditEventEntity)

    @Query("""
        UPDATE products SET stockQuantity = stockQuantity - :qty, updatedAtEpochMs = :now
        WHERE productId = :productId AND isActive = 1 AND stockQuantity >= :qty
    """)
    abstract suspend fun reduceStock(productId: Long, qty: Long, now: Long): Int

    @Query("UPDATE products SET stockQuantity = stockQuantity + :qty, updatedAtEpochMs = :now WHERE productId = :productId")
    abstract suspend fun restoreStock(productId: Long, qty: Long, now: Long): Int

    @Query("UPDATE sales SET status = :status WHERE saleId = :saleId AND status IN ('COMPLETED', 'PARTIALLY_REFUNDED')")
    abstract suspend fun updateReturnSaleStatus(saleId: String, status: String): Int

    @Query("UPDATE sale_items SET returnedQuantity = returnedQuantity + :qty WHERE lineId = :lineId AND returnedQuantity + :qty <= quantity")
    abstract suspend fun addReturnedQuantity(lineId: Long, qty: Long): Int

    @Transaction
    open suspend fun commitSale(
        sale: SaleEntity,
        items: List<SaleItemEntity>,
        payment: PaymentEntity,
        audit: AuditEventEntity,
        now: Long,
    ) {
        require(items.isNotEmpty()) { "Sale must contain at least one line" }
        require(payment.saleId == sale.saleId) { "Payment must belong to the sale" }
        insertSale(sale)
        items.forEach { line ->
            check(reduceStock(line.productId, line.quantity, now) == 1) { "Insufficient stock" }
        }
        insertItems(items)
        insertPayment(payment)
        insertAudit(audit)
    }

    /** Returns requested quantities from individual sale lines. */
    @Transaction
    open suspend fun refundSaleItems(
        saleId: String,
        requestedReturns: Map<Long, Long>,
        now: Long,
        actorId: String,
    ): Boolean {
        if (requestedReturns.isEmpty()) return false
        val status = getSaleStatus(saleId) ?: return false
        if (status != SaleStatus.COMPLETED && status != SaleStatus.PARTIALLY_REFUNDED) return false

        val items = getItemsForSale(saleId)
        val byLineId = items.associateBy { it.lineId }
        val cleanReturns = requestedReturns.filterValues { it > 0L }
        if (cleanReturns.isEmpty()) return false
        cleanReturns.forEach { (lineId, requestedQty) ->
            val line = byLineId[lineId] ?: error("Sale item not found")
            check(requestedQty <= line.quantity - line.returnedQuantity) { "Return quantity exceeds remaining quantity" }
        }

        cleanReturns.forEach { (lineId, requestedQty) ->
            val line = byLineId.getValue(lineId)
            check(addReturnedQuantity(lineId, requestedQty) == 1) { "Could not update returned quantity" }
            check(restoreStock(line.productId, requestedQty, now) == 1) { "Product not found" }
        }

        val finalItems = getItemsForSale(saleId)
        val fullyReturned = finalItems.all { it.returnedQuantity >= it.quantity }
        val nextStatus = if (fullyReturned) SaleStatus.REFUNDED else SaleStatus.PARTIALLY_REFUNDED
        check(updateReturnSaleStatus(saleId, nextStatus) == 1) { "Sale status could not be updated" }
        val totalReturned = cleanReturns.values.sum()
        insertAudit(
            AuditEventEntity(
                eventId = UUID.randomUUID().toString(),
                actorId = actorId,
                action = if (fullyReturned) "SALE_REFUNDED" else "SALE_PARTIALLY_REFUNDED",
                aggregateType = "SALE",
                aggregateId = saleId,
                reason = if (fullyReturned) "Full sale return" else "Partial product return: $totalReturned item(s)",
                approverId = null,
                createdAtEpochMs = now,
                payloadHash = "",
            )
        )
        return true
    }

    @Transaction
    open suspend fun refundSale(saleId: String, now: Long, actorId: String): Boolean {
        val status = getSaleStatus(saleId) ?: return false
        if (status != SaleStatus.COMPLETED && status != SaleStatus.PARTIALLY_REFUNDED) return false
        val remaining = getItemsForSale(saleId)
            .filter { it.quantity > it.returnedQuantity }
            .associate { it.lineId to (it.quantity - it.returnedQuantity) }
        return refundSaleItems(saleId, remaining, now, actorId)
    }
}
