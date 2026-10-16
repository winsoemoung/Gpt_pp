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

val ALL_MIGRATIONS: Array<Migration> = arrayOf(
    MIGRATION_1_2,
    MIGRATION_3_4,
    MIGRATION_4_5,
)
