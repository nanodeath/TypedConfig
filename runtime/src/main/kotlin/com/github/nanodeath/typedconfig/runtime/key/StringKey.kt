package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.checks.StringCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class StringKey(
    private val name: String,
    private val source: Source,
    default: String?,
    private val checks: List<StringCheck>
) : Key<String> {
    // this obviously doesn't do much; it's just for consistency with the other Keys.
    private val parsedDefault: String? = default?.let { parseWithName(it, name) }

    override fun resolve(): String {
        val value = source.getString(name) ?: parsedDefault ?: throw MissingConfigurationException(name)
        if (checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }

    companion object: KeyObject<String> {
        override fun parse(value: String): String = value
    }
}
