package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.MissingConfigurationException
import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.checks.IntegerCheck
import dev.madetobuild.typedconfig.runtime.source.Source

class IntegerKey(
    override val name: String,
    private val source: Source,
    default: String?,
    private val checks: List<IntegerCheck>
) : Key<Int> {
    private val parsedDefault: Int? = default?.let { parseWithName(it, name) }

    override fun resolve(): Int {
        val value = source.getInt(this) ?: parsedDefault ?: throw MissingConfigurationException(name)
        if (checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }

    companion object : KeyObject<Int> {
        override fun parse(value: String): Int =
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                throw ParseException("Not an integer: '$value'")
            }
    }
}
