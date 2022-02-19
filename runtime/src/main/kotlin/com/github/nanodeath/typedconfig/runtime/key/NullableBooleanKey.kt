package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableBooleanKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val constraints: List<Unit>
) : Key<Boolean?> {
    override fun resolve(): Boolean? = source.getBoolean(name)
}
