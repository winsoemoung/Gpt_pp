package com.example.offlinepos.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "return_transactions",
    indices = [Index("saleId"), Index("createdAtEpochMs")],
)
data class ReturnTransactionEntity(
    @PrimaryKey val returnId: String,
    val saleId: String,
    val returnedAmountMinor: Long,
    val refundMethod: String = "CASH",
    val refundReference: String? = null,
    val reason: String? = null,
    val actorId: String,
    val createdAtEpochMs: Long,
)
