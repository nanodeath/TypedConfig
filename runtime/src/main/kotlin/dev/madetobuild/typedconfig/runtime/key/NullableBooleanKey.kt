package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.source.Source

class NullableBooleanKey(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Boolean?> {
    override fun resolve(): Boolean? = source.getBoolean(this)
}
