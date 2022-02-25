package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.checks.DoubleCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableDoubleKey(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val checks: List<DoubleCheck>
) : Key<Double?> {
    override fun resolve(): Double? {
        val value = source.getDouble(this)
        if (value != null && checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }
}
