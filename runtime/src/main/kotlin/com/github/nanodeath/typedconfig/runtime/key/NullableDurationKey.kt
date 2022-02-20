package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.source.Source
import java.time.Duration

class NullableDurationKey(
    private val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    @Suppress("unused") private val constraints: List<Unit>
) : Key<Duration?> {
    override fun resolve(): Duration? = source.getString(name)?.let { DurationKey.parseDuration(it, name) }
}
