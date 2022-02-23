package com.github.nanodeath.typedconfig.runtime.source

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MultiSource(private val sources: List<Source>) : Source {
    init {
        log.trace("Initializing MultiSource using these sources: {}", sources)
    }

    override fun getInt(key: String): Int? = sources.firstNotNullOfOrNull { it.getInt(key) }
    override fun getString(key: String): String? = sources.firstNotNullOfOrNull { it.getString(key) }
    override fun getDouble(key: String): Double? = sources.firstNotNullOfOrNull { it.getDouble(key) }
    override fun getBoolean(key: String): Boolean? = sources.firstNotNullOfOrNull { it.getBoolean(key) }
    override fun getList(key: String): List<String>? = sources.firstNotNullOfOrNull { it.getList(key) }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(MultiSource::class.java)
    }
}
