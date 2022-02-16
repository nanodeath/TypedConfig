package name.maxaller.konfigurator.runtime

import java.util.concurrent.ConcurrentHashMap

class CachedSource(private val delegate: Source) : Source {
    private val cache = ConcurrentHashMap<String, Any?>()
    override fun getInt(key: String): Int? {
        if (!cache.containsKey(key)) {
            cache.putIfAbsent(key, delegate.getInt(key))
        }
        return cache[key] as Int?
    }

    override fun getString(key: String): String? {
        if (!cache.containsKey(key)) {
            cache.putIfAbsent(key, delegate.getInt(key))
        }
        return cache[key] as String?
    }
}

fun Source.cached(): Source = if (this is CachedSource) this else CachedSource(this)