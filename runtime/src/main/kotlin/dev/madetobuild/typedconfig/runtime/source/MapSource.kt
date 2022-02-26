package dev.madetobuild.typedconfig.runtime.source

import dev.madetobuild.typedconfig.runtime.KeyCase
import dev.madetobuild.typedconfig.runtime.KeyCase.LOWER_CAMEL_CASE
import dev.madetobuild.typedconfig.runtime.KeyCase.SCREAMING_SNAKE_CASE
import dev.madetobuild.typedconfig.runtime.camelToUpperSnake
import dev.madetobuild.typedconfig.runtime.key.*

class MapSource(private val map: Map<String, Any>, private val keyCase: KeyCase = LOWER_CAMEL_CASE) : Source {
    override fun getInt(key: Key<*>): Int? =
        when (val value = map[convertKey(key.name)]) {
            is Int -> value
            is String -> IntegerKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getString(key: Key<*>): String? =
        when (val value = map[convertKey(key.name)]) {
            is String -> value
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getDouble(key: Key<*>): Double? =
        when (val value = map[convertKey(key.name)]) {
            is Double -> value
            is String -> DoubleKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getBoolean(key: Key<*>): Boolean? =
        when (val value = map[convertKey(key.name)]) {
            is Boolean -> value
            is String -> BooleanKey.parse(value)
            null -> null
            else -> throw IllegalArgumentException("Unexpected type in map: ${value.javaClass}")
        }

    override fun getList(key: Key<*>): List<String>? =
        when (val value = map[convertKey(key.name)]) {
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
