package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source

class ListKey<T>(
    private val name: String,
    private val source: Source,
    private val default: List<String>?,
    @Suppress("unused") private val checks: List<Unit>,
    private val parse: (String) -> T
) : Key<List<T>> {
    @Suppress("TooGenericExceptionCaught")
    override fun resolve(): List<T> {
        val strings = source.getList(name) ?: default ?: throw MissingConfigurationException(name)
        return strings.mapIndexed { index, s: String ->
            try {
                parse(s)
            } catch (e: Exception) {
                // TODO we could add some better information here if we knew the name of the inner type
                throw ParseException("$name[$index]", "Failed to parse `$name[$index]`: " +
                        "failed to parse '$s'", e)
            }
        }
    }

    companion object : KeyObject<List<String>> {
        override fun parse(value: String): List<String> {
            val list = value.splitToSequence(',').map { it.trim() }.toList()
            // If the value contains an empty string, we want emptyList(), not listOf("")
            return if (list.singleOrNull()?.isBlank() == true) emptyList() else list
        }
    }
}
