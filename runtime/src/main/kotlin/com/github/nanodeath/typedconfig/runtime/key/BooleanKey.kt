package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source

class BooleanKey(
    private val name: String,
    private val source: Source,
    private val default: Boolean?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Boolean> {
    override fun resolve(): Boolean =
        source.getBoolean(name) ?: default ?: throw MissingConfigurationException(name)

    companion object : KeyObject<Boolean> {
        override fun parse(value: String): Boolean =
            if (value.equals("true", ignoreCase = true)) {
                true
            } else if (value.equals("false", ignoreCase = true)) {
                false
            } else {
                throw ParseException("Not a boolean: $value")
            }
    }
}
