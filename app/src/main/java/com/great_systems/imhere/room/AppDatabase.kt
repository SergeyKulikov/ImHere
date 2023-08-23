package com.great_systems.imhere.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.great_systems.imhere.entity.ContactItem
import com.great_systems.sysdb.RoomTypeConverter

/**
 * Системная база данных для хранения служебной информации.
 */
@Database(
    entities = [
        ContactItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun app(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                    INSTANCE = it
                }
            }
        }
    }
}