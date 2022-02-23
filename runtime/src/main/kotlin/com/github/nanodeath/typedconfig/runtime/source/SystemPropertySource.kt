package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.appendKey
import com.github.nanodeath.typedconfig.runtime.key.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SystemPropertySource : Source {
    override fun getInt(key: String): Int? = key.parseUsing(IntegerKey::parse)
        .also { log.debug("getInt({}) => {}", key, it) }

    override fun getString(key: String): String? = key.parseUsing(StringKey::parse)
        .also { log.debug("getString({}) => {}", key, it) }

    override fun getDouble(key: String): Double? = key.parseUsing(DoubleKey::parse)
        .also { log.debug("getDouble({}) => {}", key, it) }

    override fun getBoolean(key: String): Boolean? = key.parseUsing(BooleanKey::parse)
        .also { log.debug("getBoolean({}) => {}", key, it) }

    override fun getList(key: String): List<String>? = key.parseUsing(ListKey.Companion::parse)
        .also { log.debug("getList({}) => {}", key, it) }

    private inline fun <T> String.parseUsing(callback: (String) -> T): T? =
        try {
            System.getProperty(this)?.let(callback)
        } catch (e: ParseException) {
            throw e.appendKey(this)
        }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(SystemPropertySource::class.java)
    }
}
