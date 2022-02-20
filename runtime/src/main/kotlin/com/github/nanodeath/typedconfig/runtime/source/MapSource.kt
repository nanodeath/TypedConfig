package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.KeyCase
import com.github.nanodeath.typedconfig.runtime.KeyCase.LOWER_CAMEL_CASE
import com.github.nanodeath.typedconfig.runtime.KeyCase.SCREAMING_SNAKE_CASE
import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake
import com.github.nanodeath.typedconfig.runtime.key.*

class MapSource(private val map: Map<String, Any>, private val keyCase: KeyCase = LOWER_CAMEL_CASE) : Source {
    override fun getInt(key: String): Int? =
        when (val value = map[convertKey(key)]) {
            is Int -> value
            is String -> IntegerKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getString(key: String): String? =
        when (val value = map[convertKey(key)]) {
            is String -> value
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getDouble(key: String): Double? =
        when (val value = map[convertKey(key)]) {
            is Double -> value
            is String -> DoubleKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getBoolean(key: String): Boolean? =
        when (val value = map[convertKey(key)]) {
            is Boolean -> value
            is String -> BooleanKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getList(key: String): List<String>? =
        when (val value = map[convertKey(key)]) {
            is List<*> -> value.map(Any?::toString)
            is String -> ListKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    private fun convertKey(key: String): String =
        when (keyCase) {
            LOWER_CAMEL_CASE -> key
            SCREAMING_SNAKE_CASE -> key.camelToUpperSnake()
        }
}
