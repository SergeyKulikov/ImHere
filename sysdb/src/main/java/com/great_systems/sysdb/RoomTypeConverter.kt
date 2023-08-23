package com.great_systems.sysdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class RoomTypeConverter {

    @TypeConverter
    fun fromArrayString(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toArrayString(value: String): List<String>  {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }


}
