package com.example.offlinepos.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sync_tombstones",
    indices = [Index(value = ["entityType", "entityId"], unique = true)],
)
data class SyncTombstoneEntity(
    @PrimaryKey val tombstoneId: String,
    val entityType: String,
    val entityId: String,
    val deletedAtEpochMs: Long,
    val deletedByDeviceId: String,
)
