package com.great_systems.sysdb.entity

import java.util.UUID


data class UserEntity (
    val uuidCompany: UUID, // Ключ компании
    val uuid: UUID, // Ключ пользователя
    val name: String, // Имя для отображения
    val active: Boolean = true // флаг блокировки пользователя для работы в системе
)
