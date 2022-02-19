package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.camelToUpperSnake

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())
    override fun getInt(key: String): Int? = env.get(key.camelToUpperSnake())?.toIntOrNull()
    override fun getString(key: String): String? = env.get(key.camelToUpperSnake())
    override fun getDouble(key: String): Double? = env.get(key.camelToUpperSnake())?.toDoubleOrNull()
    override fun getBoolean(key: String): Boolean? = env.get(key.camelToUpperSnake())?.toBoolean()
}

internal class Env {
    fun get(key: String): String? = System.getenv(key)
}
