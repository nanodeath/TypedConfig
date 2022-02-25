package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.source.Source
import java.time.Duration

class NullableDurationKey(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Duration?> {
    override fun resolve(): Duration? = source.getString(this)?.let { DurationKey.parse(it) }
}
