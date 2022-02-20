package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.appendKey
import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake
import com.github.nanodeath.typedconfig.runtime.key.*

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())

    override fun getInt(key: String): Int? = key.parseUsing(IntegerKey::parse)
    override fun getString(key: String): String? = key.parseUsing(StringKey::parse)
    override fun getDouble(key: String): Double? = key.parseUsing(DoubleKey::parse)
    override fun getBoolean(key: String): Boolean? = key.parseUsing(BooleanKey::parse)
    override fun getList(key: String): List<String>? = key.parseUsing(ListKey.Companion::parse)

    private inline fun <T> String.parseUsing(callback: (String) -> T): T? =
        try {
            env.get(this.camelToUpperSnake())?.let(callback)
        } catch (e: ParseException) {
            throw e.appendKey(this)
        }
}

internal class Env {
    fun get(key: String): String? = System.getenv(key)
}
