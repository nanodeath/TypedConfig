package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake
import com.github.nanodeath.typedconfig.runtime.key.*

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())
    override fun getInt(key: String): Int? = env.get(key.camelToUpperSnake())?.let(IntegerKey::parse)
    override fun getString(key: String): String? = env.get(key.camelToUpperSnake())?.let(StringKey::parse)
    override fun getDouble(key: String): Double? = env.get(key.camelToUpperSnake())?.let(DoubleKey::parse)
    override fun getBoolean(key: String): Boolean? = env.get(key.camelToUpperSnake())?.let(BooleanKey::parse)
    override fun getList(key: String): List<String>? = env.get(key.camelToUpperSnake())?.let(ListKey.Companion::parse)
}

internal class Env {
    fun get(key: String): String? = System.getenv(key)
}
