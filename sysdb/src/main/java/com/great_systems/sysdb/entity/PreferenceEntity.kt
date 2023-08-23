package com.great_systems.sysdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["company_uuid", "user_uuid", "pref_type"])
class PreferenceEntity {
    @ColumnInfo(name = "company_uuid")
    var companyUUID: UUID = UUID(0,0)

    @ColumnInfo(name = "user_uuid")
    var userUUID: UUID = UUID(0,0)

    @ColumnInfo(name = "pref_type")
    var type: EPrefType = EPrefType.pftCompany

    @ColumnInfo(name = "pref_data")
    var prefData: String = ""
}