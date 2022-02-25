package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.key.Key
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MultiSource(private val sources: List<Source>) : Source {
    init {
        log.trace("Initializing MultiSource using these sources: {}", sources)
    }

    override fun getInt(key: Key<*>): Int? = sources.firstNotNullOfOrNull { it.getInt(key) }
    override fun getString(key: Key<*>): String? = sources.firstNotNullOfOrNull { it.getString(key) }
    override fun getDouble(key: Key<*>): Double? = sources.firstNotNullOfOrNull { it.getDouble(key) }
    override fun getBoolean(key: Key<*>): Boolean? = sources.firstNotNullOfOrNull { it.getBoolean(key) }
    override fun getList(key: Key<*>): List<String>? = sources.firstNotNullOfOrNull { it.getList(key) }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(MultiSource::class.java)
    }
}
