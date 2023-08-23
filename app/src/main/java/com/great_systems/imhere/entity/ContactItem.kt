package com.great_systems.imhere.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ContactItem() {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name = ""
    var photo_id: Long? = null
    var photo_uri: String? = null
    var phones: MutableList<String> = mutableListOf()

    constructor(id: Long, name: String, photo_id: Long? = null, photo_uri: String? = null,
                phone: MutableList<String>): this() {
        this.id = id
        this.name = name
        this.photo_id = photo_id
        this.photo_uri = photo_uri
        this.phones.addAll(phone)
    }

    fun phonesListString(): String {
        val buffer = StringBuffer()
        for (i in this.phones.indices) {
            buffer.append(if (i == 0) "" else ", ")
            buffer.append(this.phones[i])
        }
        return buffer.toString()
    }

}