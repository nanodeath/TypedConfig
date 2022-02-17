package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.KeyCase
import com.github.nanodeath.typedconfig.runtime.KeyCase.LOWER_CAMEL_CASE
import com.github.nanodeath.typedconfig.runtime.KeyCase.SCREAMING_SNAKE_CASE
import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake

class MapSource(private val map: Map<String, Any>, private val keyCase: KeyCase = LOWER_CAMEL_CASE) : Source {
    override fun getInt(key: String): Int? {
        return when (val value = map[convertKey(key)]) {
            is String -> value.toIntOrNull()
            is Int -> value
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }
    }

    override fun getString(key: String): String? {
        return when (val value = map[convertKey(key)]) {
            is String -> value
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }
    }

    override fun getDouble(key: String): Double? {
        return when (val value = map[convertKey(key)]) {
            is String -> value.toDoubleOrNull()
            is Double -> value
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }
    }

    private fun convertKey(key: String): String {
        val realKey = when (keyCase) {
            LOWER_CAMEL_CASE -> key
            SCREAMING_SNAKE_CASE -> key.camelToUpperSnake()
        }
        return realKey
    }
}
