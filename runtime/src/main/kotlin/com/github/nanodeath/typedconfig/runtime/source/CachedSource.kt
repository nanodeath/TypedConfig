package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.key.Key
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

private val logger = LoggerFactory.getLogger(CachedSource::class.java)

class CachedSource(private val delegate: Source) : Source {
    private val cache = ConcurrentHashMap<String, Any?>()
    override fun getInt(key: Key<*>): Int? {
        val name = key.name
        if (!cache.containsKey(name)) {
            val value = delegate.getInt(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(name, value)
        }
        return cache[name] as Int?
    }

    override fun getString(key: Key<*>): String? {
        val name = key.name
        if (!cache.containsKey(name)) {
            val value = delegate.getString(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(name, value)
        }
        return cache[name] as String?
    }

    override fun getDouble(key: Key<*>): Double? {
        val name = key.name
        if (!cache.containsKey(name)) {
            val value = delegate.getDouble(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(name, value)
        }
        return cache[name] as Double?
    }

    override fun getBoolean(key: Key<*>): Boolean? {
        val name = key.name
        if (!cache.containsKey(name)) {
            val value = delegate.getBoolean(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(name, value)
        }
        return cache[name] as Boolean?
    }

    override fun getList(key: Key<*>): List<String>? {
        val name = key.name
        if (!cache.containsKey(name)) {
            val value = delegate.getList(key)
            logger.trace("Caching {} -> {}", key, value)
            cache.putIfAbsent(name, value)
        }
        @Suppress("UNCHECKED_CAST")
        return cache[name] as List<String>?
    }
}

fun Source.cached(): Source = if (this is CachedSource) this else CachedSource(this)
