package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.source.Source

class NullableListKey<T>(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val checks: List<Unit>,
    private val parse: (String) -> T
) : Key<List<T>?> {
    override fun resolve(): List<T>? = source.getList(this)?.map(parse)
}

fun <T> NullableListKey<T>.asSet(): Key<Set<T>?> = object : Key<Set<T>?> {
    override val name = this@asSet.name
    override fun resolve(): Set<T>? = this@asSet.resolve()?.toSet()
}
