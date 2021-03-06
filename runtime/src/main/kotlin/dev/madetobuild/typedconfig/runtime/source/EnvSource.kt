package dev.madetobuild.typedconfig.runtime.source

import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.appendKey
import dev.madetobuild.typedconfig.runtime.camelToUpperSnake
import dev.madetobuild.typedconfig.runtime.key.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())

    override fun getInt(key: Key<*>): Int? = key.parseUsing(IntegerKey::parse)
        .also { log.debug("getInt({}) => {}", key, it) }

    override fun getString(key: Key<*>): String? = key.parseUsing(StringKey::parse)
        .also { log.debug("getString({}) => {}", key, key.maskIfSensitive(it)) }

    override fun getDouble(key: Key<*>): Double? = key.parseUsing(DoubleKey::parse)
        .also { log.debug("getDouble({}) => {}", key, it) }

    override fun getBoolean(key: Key<*>): Boolean? = key.parseUsing(BooleanKey::parse)
        .also { log.debug("getBoolean({}) => {}", key, it) }

    override fun getList(key: Key<*>): List<String>? = key.parseUsing(ListKey.Companion::parse)
        .also { list ->
            if (log.isDebugEnabled) {
                log.debug("getList({}) => {}", key, list?.map(key::maskIfSensitive))
            }
        }

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
