package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.constraints.IntConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableIntKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val constraints: List<IntConstraint>
) : Key<Int?> {
    override fun resolve(): Int? {
        val value = source.getInt(name)
        if (value != null && constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
