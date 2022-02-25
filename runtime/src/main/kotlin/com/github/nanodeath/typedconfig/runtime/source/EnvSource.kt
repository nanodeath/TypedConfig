package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.appendKey
import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake
import com.github.nanodeath.typedconfig.runtime.key.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())

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
            env.get(this.name.camelToUpperSnake())?.let(callback)
        } catch (e: ParseException) {
            throw e.appendKey(this)
        }

    private companion object {
        val log: Logger = LoggerFactory.getLogger(EnvSource::class.java)
    }
}

internal class Env {
    fun get(key: String): String? = System.getenv(key)
}
