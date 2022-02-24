package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.source.Source

class NullableListKey<T>(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val checks: List<Unit>,
    private val parse: (String) -> T
) : Key<List<T>?> {
    override fun resolve(): List<T>? = source.getList(name)?.map(parse)
}

fun <T> NullableListKey<T>.asSet(): Key<Set<T>?> = object : Key<Set<T>?> {
    override fun resolve(): Set<T>? = this@asSet.resolve()?.toSet()
}
