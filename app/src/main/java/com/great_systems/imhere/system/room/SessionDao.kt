package com.great_systems.imhere.system.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.great_systems.imhere.system.room.entity.EPrefType
import com.great_systems.imhere.system.room.entity.PreferenceEntity
import java.util.UUID

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: PreferenceEntity): Long

    @Query("SELECT * FROM PreferenceEntity WHERE company_uuid = :companyUUID AND user_uuid = :userUUID AND pref_type = :prefType")
    fun select(companyUUID: UUID, userUUID: UUID, prefType: EPrefType): PreferenceEntity

}
