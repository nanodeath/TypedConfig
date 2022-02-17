package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.constraints.DoubleConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class DoubleKey(
    private val name: String,
    private val source: Source,
    private val default: Double?,
    private val constraints: List<DoubleConstraint>
) : Key<Double> {
    override fun resolve(): Double {
        val value = source.getDouble(name) ?: default ?: throw MissingConfigurationException(name)
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
