package com.great_systems.imhere.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.great_systems.imhere.entity.ContactItem
import java.util.concurrent.Flow

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContactList(list: List<ContactItem>): List<Long>

    @Query("DELETE FROM ContactItem WHERE 1")
    fun removeAllContact()

    @Query("SELECT * FROM ContactItem ORDER by name")
    fun selectContactList(): List<ContactItem>

    @Query("SELECT * FROM ContactItem ORDER by name")
    fun selectContactListLD(): LiveData<List<ContactItem>>
}
