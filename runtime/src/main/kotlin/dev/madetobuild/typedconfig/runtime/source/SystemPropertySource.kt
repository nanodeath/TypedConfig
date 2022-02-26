package dev.madetobuild.typedconfig.runtime.source

import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.appendKey
import dev.madetobuild.typedconfig.runtime.key.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SystemPropertySource : Source {
    override fun getInt(key: Key<*>): Int? = key.parseUsing(IntegerKey::parse)
        .also { log.debug("getInt({}) => {}", key, it) }

    override fun getString(key: Key<*>): String? = key.parseUsing(StringKey::parse)
        .also { log.debug("getString({}) => {}", key, it) }

    override fun getDouble(key: Key<*>): Double? = key.parseUsing(DoubleKey::parse)
        .also { log.debug("getDouble({}) => {}", key, it) }

    override fun getBoolean(key: Key<*>): Boolean? = key.parseUsing(BooleanKey::parse)
        .also { log.debug("getBoolean({}) => {}", key, it) }

    override fun getList(key: Key<*>): List<String>? = key.parseUsing(ListKey.Companion::parse)
        .also { log.debug("getList({}) => {}", key, it) }

    private inline fun <T> Key<*>.parseUsing(callback: (String) -> T): T? =
        try {
            System.getProperty(this.name)?.let(callback)
        } catch (e: ParseException) {
            throw e.appendKey(this)
        }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(SystemPropertySource::class.java)
    }
}
