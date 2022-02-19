package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.constraints.StringConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableStringKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val constraints: List<StringConstraint>
) : Key<String?> {
    override fun resolve(): String? {
        val value = source.getString(name)
        if (value != null && constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
