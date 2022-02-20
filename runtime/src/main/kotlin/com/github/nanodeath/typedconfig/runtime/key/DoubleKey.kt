package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.checks.DoubleCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class DoubleKey(
    private val name: String,
    private val source: Source,
    private val default: Double?,
    private val checks: List<DoubleCheck>
) : Key<Double> {
    override fun resolve(): Double {
        val value = source.getDouble(name) ?: default ?: throw MissingConfigurationException(name)
        if (checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }

    companion object : KeyObject<Double> {
        override fun parse(value: String): Double =
            try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                throw ParseException("Not a double: '$value'")
            }
    }
}
