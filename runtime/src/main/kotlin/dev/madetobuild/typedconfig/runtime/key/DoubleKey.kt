package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.MissingConfigurationException
import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.checks.DoubleCheck
import dev.madetobuild.typedconfig.runtime.source.Source

class DoubleKey(
    override val name: String,
    private val source: Source,
    default: String?,
    private val checks: List<DoubleCheck>
) : Key<Double> {
    private val parsedDefault: Double? = default?.let { parseWithName(it, name) }

    override fun resolve(): Double {
        val value = source.getDouble(this) ?: parsedDefault ?: throw MissingConfigurationException(name)
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
