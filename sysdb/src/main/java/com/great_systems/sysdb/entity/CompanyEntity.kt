package com.great_systems.sysdb.entity

import java.util.UUID

/**
 * Предусматриваем ведение на одной программе нескольких компаний
 * Первоначально ключ шифрования совпадает с ключом компании, если будет
 * скомпрометирован ключ, то его можно будет поменять
 */

data class CompanyEntity(
    val uuid: UUID, // ключ компаниия
    val security: UUID, // ключ шифрования (ключ компании) он определяет в какой БД храним данные
    val useSecurity: Boolean = false,
    val name: String,
)
