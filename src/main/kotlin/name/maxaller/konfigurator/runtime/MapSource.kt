package name.maxaller.konfigurator.runtime

import name.maxaller.konfigurator.runtime.KeyCase.LOWER_CAMEL_CASE
import name.maxaller.konfigurator.runtime.KeyCase.SCREAMING_SNAKE_CASE

class MapSource(private val map: Map<String, Any>, private val caseOfKeys: KeyCase = LOWER_CAMEL_CASE) : Source {
    override fun getInt(key: String): Int? {
        val realKey = when (caseOfKeys) {
            LOWER_CAMEL_CASE -> key
            SCREAMING_SNAKE_CASE -> key.camelToUpperSnake()
        }
        return when (val value = map[realKey]) {
            is String -> value.toIntOrNull()
            is Int -> value
            else -> throw IllegalArgumentException("Unexpected type in map: ${value?.javaClass}")
        }
    }
}
