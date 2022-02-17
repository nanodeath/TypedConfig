package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.constraints.IntConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class IntKey(
    private val name: String,
    private val source: Source,
    private val default: Int?,
    private val constraints: List<IntConstraint>
) : Key<Int> {
    override fun resolve(): Int {
        val value = source.getInt(name) ?: default ?: throw MissingConfigurationException(name)
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
