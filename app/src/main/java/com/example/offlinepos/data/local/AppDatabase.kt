package com.example.offlinepos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ProductEntity::class,
        SaleEntity::class,
        SaleItemEntity::class,
        PaymentEntity::class,
        AuditEventEntity::class,
        CustomerEntity::class,
        DebtTransactionEntity::class,
        PurchaseEntity::class,
        ShiftReportEntity::class,
        ParkedOrderEntity::class,
        StockMovementEntity::class,
        ReturnTransactionEntity::class,
        OutboxEntity::class,
    ],
    version = 9,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun checkoutDao(): CheckoutDao
    abstract fun customerDao(): CustomerDao
    abstract fun operationsDao(): OperationsDao
    abstract fun outboxDao(): OutboxDao

    companion object {
        private const val DB_NAME = "offline_pos.db"
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9)
                    .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            db.execSQL("PRAGMA busy_timeout=30000")
                        }
                    })
                    .build().also { INSTANCE = it }
            }

        fun closeAndReset() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
            }
        }
    }
}
