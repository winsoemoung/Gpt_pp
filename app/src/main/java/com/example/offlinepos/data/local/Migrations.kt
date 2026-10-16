package com.example.offlinepos.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE products ADD COLUMN category TEXT NOT NULL DEFAULT 'အထွေထွေ'")
        db.execSQL("ALTER TABLE products ADD COLUMN isRoofingSheet INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE products ADD COLUMN lowStockThreshold INTEGER NOT NULL DEFAULT 3")
        db.execSQL("ALTER TABLE products ADD COLUMN imageUri TEXT")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE sales ADD COLUMN deliveryFeeMinor INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS stock_movements (
            movementId TEXT NOT NULL PRIMARY KEY,
            productId INTEGER NOT NULL,
            movementType TEXT NOT NULL,
            quantityChange INTEGER NOT NULL,
            quantityBefore INTEGER NOT NULL,
            quantityAfter INTEGER NOT NULL,
            referenceId TEXT,
            reason TEXT,
            actorId TEXT NOT NULL,
            createdAtEpochMs INTEGER NOT NULL
        )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_stock_movements_productId ON stock_movements(productId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_stock_movements_createdAtEpochMs ON stock_movements(createdAtEpochMs)")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE sale_items ADD COLUMN returnedQuantity INTEGER NOT NULL DEFAULT 0")
    }
}
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS return_transactions (
            returnId TEXT NOT NULL PRIMARY KEY,
            saleId TEXT NOT NULL,
            returnedAmountMinor INTEGER NOT NULL,
            refundMethod TEXT NOT NULL,
            refundReference TEXT,
            reason TEXT,
            actorId TEXT NOT NULL,
            createdAtEpochMs INTEGER NOT NULL
        )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_return_transactions_saleId ON return_transactions(saleId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_return_transactions_createdAtEpochMs ON return_transactions(createdAtEpochMs)")
    }
}
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE customers ADD COLUMN creditDueDateEpochMs INTEGER")
    }
}
val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""CREATE TABLE IF NOT EXISTS sync_outbox (
            operationId TEXT NOT NULL PRIMARY KEY,
            idempotencyKey TEXT NOT NULL,
            aggregateType TEXT NOT NULL,
            aggregateId TEXT NOT NULL,
            payloadJson TEXT NOT NULL,
            schemaVersion INTEGER NOT NULL,
            aggregateVersion INTEGER NOT NULL,
            actorId TEXT NOT NULL,
            deviceId TEXT NOT NULL,
            state TEXT NOT NULL,
            errorClass TEXT,
            lastError TEXT,
            retryCount INTEGER NOT NULL,
            nextAttemptAtEpochMs INTEGER NOT NULL,
            reconciliationResult TEXT,
            createdAtEpochMs INTEGER NOT NULL,
            updatedAtEpochMs INTEGER NOT NULL
        )""")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sync_outbox_state ON sync_outbox(state)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_sync_outbox_operationId ON sync_outbox(operationId)")
    }
}
val ALL_MIGRATIONS: Array<Migration> = arrayOf(
    MIGRATION_1_2,
    MIGRATION_3_4,
    MIGRATION_4_5,
    MIGRATION_5_6,
    MIGRATION_6_7,
    MIGRATION_7_8,
    MIGRATION_8_9,
)
