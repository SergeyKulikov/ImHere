package com.great_systems.imhere.entity

import androidx.room.Entity

@Entity
class ContactItem {
    private var _name = ""
    private var _phone = ""
    private var _my_id = 0

    fun setName(name: String) {
        _name = name
    }

    fun setPhone(phone: String) {
        _phone = phone
    }

    fun setMyId(my_id: Int) {
        _my_id = my_id
    }
}