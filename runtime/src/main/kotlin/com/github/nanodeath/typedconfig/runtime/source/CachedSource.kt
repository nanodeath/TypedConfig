package com.github.nanodeath.typedconfig.runtime.source

import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

private val logger = LoggerFactory.getLogger(CachedSource::class.java)

class CachedSource(private val delegate: Source) : Source {
    private val cache = ConcurrentHashMap<String, Any?>()
    override fun getInt(key: String): Int? {
        if (!cache.containsKey(key)) {
            val value = delegate.getInt(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(key, value)
        }
        return cache[key] as Int?
    }

    override fun getString(key: String): String? {
        if (!cache.containsKey(key)) {
            val value = delegate.getString(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(key, value)
        }
        return cache[key] as String?
    }

    override fun getDouble(key: String): Double? {
        if (!cache.containsKey(key)) {
            val value = delegate.getDouble(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(key, value)
        }
        return cache[key] as Double?
    }

    override fun getBoolean(key: String): Boolean? {
        if (!cache.containsKey(key)) {
            val value = delegate.getBoolean(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(key, value)
        }
        return cache[key] as Boolean?
    }

    override fun getList(key: String): List<String>? {
        if (!cache.containsKey(key)) {
            val value = delegate.getList(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(key, value)
        }
        @Suppress("UNCHECKED_CAST")
        return cache[key] as List<String>?
    }
}

fun Source.cached(): Source = if (this is CachedSource) this else CachedSource(this)
