package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.checks.IntCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class IntKey(
    private val name: String,
    private val source: Source,
    private val default: Int?,
    private val checks: List<IntCheck>
) : Key<Int> {
    override fun resolve(): Int {
        val value = source.getInt(name) ?: default ?: throw MissingConfigurationException(name)
        if (checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }

    companion object : KeyObject<Int> {
        override fun parse(value: String): Int = value.toInt()
    }
}
