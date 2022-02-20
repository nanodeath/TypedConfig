package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.checks.IntCheck
import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableIntKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val checks: List<IntCheck>
) : Key<Int?> {
    override fun resolve(): Int? {
        val value = source.getInt(name)
        if (value != null && checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }
}
