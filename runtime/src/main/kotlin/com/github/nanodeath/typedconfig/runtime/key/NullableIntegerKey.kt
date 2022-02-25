package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.checks.IntegerCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableIntegerKey(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val checks: List<IntegerCheck>
) : Key<Int?> {
    override fun resolve(): Int? {
        val value = source.getInt(this)
        if (value != null && checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }
}
