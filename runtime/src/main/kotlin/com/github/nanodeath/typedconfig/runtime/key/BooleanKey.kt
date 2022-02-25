package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source

class BooleanKey(
    override val name: String,
    private val source: Source,
    default: String?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Boolean> {
    private val parsedDefault: Boolean? = default?.let { parseWithName(it, name) }

    override fun resolve(): Boolean =
        source.getBoolean(this) ?: parsedDefault ?: throw MissingConfigurationException(name)

    companion object : KeyObject<Boolean> {
        override fun parse(value: String): Boolean =
            if (value.equals("true", ignoreCase = true)) {
                true
            } else if (value.equals("false", ignoreCase = true)) {
                false
            } else {
                throw ParseException("Not a boolean: '$value'")
            }
    }
}
