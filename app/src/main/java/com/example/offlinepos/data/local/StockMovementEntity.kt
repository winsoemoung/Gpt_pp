package com.example.offlinepos.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stock_movements",
    indices = [Index("productId"), Index("createdAtEpochMs")],
)
data class StockMovementEntity(
    @PrimaryKey val movementId: String,
    val productId: Long,
    val movementType: String,
    val quantityChange: Long,
    val quantityBefore: Long,
    val quantityAfter: Long,
    val referenceId: String? = null,
    val reason: String? = null,
    val actorId: String = "SanThit",
    val createdAtEpochMs: Long,
)
