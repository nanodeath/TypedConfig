package com.github.nanodeath.typedconfig.runtime.source

import com.fasterxml.jackson.jr.ob.JSON
import com.github.nanodeath.typedconfig.runtime.key.Key
import java.io.File

class JsonSource internal constructor(private val document: Map<String, Any>) : Source {
    constructor(file: File) : this(JSON.std.mapFrom(file.bufferedReader()))

    override fun getInt(key: Key<*>): Int? = lookup(key) as? Int

    override fun getString(key: Key<*>): String? = lookup(key) as? String

    override fun getDouble(key: Key<*>): Double? = lookup(key) as? Double

    override fun getBoolean(key: Key<*>): Boolean? = lookup(key) as Boolean?

    override fun getList(key: Key<*>): List<String>? = (lookup(key) as List<*>?)?.map { it.toString() }

    private fun lookup(key: Key<*>) = key.name
        .splitToSequence('.')
        .fold<String, Any?>(document) { current, subkey ->
            (current as? Map<*, *>)?.get(subkey)
        }
}
