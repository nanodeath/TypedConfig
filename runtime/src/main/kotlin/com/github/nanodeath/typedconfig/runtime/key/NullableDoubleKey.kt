package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.constraints.DoubleConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableDoubleKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val constraints: List<DoubleConstraint>
) : Key<Double?> {
    override fun resolve(): Double? {
        val value = source.getDouble(name)
        if (value != null && constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
