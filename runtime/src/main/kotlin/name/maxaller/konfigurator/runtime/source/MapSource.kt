package name.maxaller.konfigurator.runtime.source

import name.maxaller.konfigurator.runtime.KeyCase
import name.maxaller.konfigurator.runtime.KeyCase.LOWER_CAMEL_CASE
import name.maxaller.konfigurator.runtime.KeyCase.SCREAMING_SNAKE_CASE
import name.maxaller.konfigurator.runtime.camelToUpperSnake

class MapSource(private val map: Map<String, Any>, private val caseOfKeys: KeyCase = LOWER_CAMEL_CASE) : Source {
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

    private fun convertKey(key: String): String {
        val realKey = when (caseOfKeys) {
            LOWER_CAMEL_CASE -> key
            SCREAMING_SNAKE_CASE -> key.camelToUpperSnake()
        }
        return realKey
    }
}
