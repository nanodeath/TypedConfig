package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableSetKey<T>(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val checks: List<Unit>,
    private val parse: (String) -> T
) : Key<Set<T>?> {
    override fun resolve(): Set<T>? = source.getList(name)?.map(parse)?.toSet()
}
