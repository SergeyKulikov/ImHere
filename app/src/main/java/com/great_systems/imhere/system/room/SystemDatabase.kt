package com.great_systems.imhere.system.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.great_systems.imhere.system.room.entity.PreferenceData
import com.great_systems.imhere.system.room.entity.PreferenceEntity

/**
 * Системная база данных для хранения служебной информации.
 */
@Database(
    entities = [
        PreferenceEntity::class
    ],
    version = 1,
    exportSchema = false
)
// @TypeConverters(RoomTypeConverter::class)
abstract class SystemDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: SystemDatabase? = null

        fun getDatabase(context: Context): SystemDatabase {
            return synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SystemDatabase::class.java,
                    "system.db"
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