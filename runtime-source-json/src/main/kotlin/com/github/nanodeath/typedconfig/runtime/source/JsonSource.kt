package com.github.nanodeath.typedconfig.runtime.source

import com.fasterxml.jackson.jr.ob.JSON
import java.io.File

class JsonSource internal constructor(private val document: Map<String, Any>) : Source {
    constructor(file: File) : this(JSON.std.mapFrom(file.bufferedReader()))

    override fun getInt(key: String): Int? = lookup(key) as? Int

    override fun getString(key: String): String? = lookup(key) as? String

    override fun getDouble(key: String): Double? = lookup(key) as? Double

    override fun getBoolean(key: String): Boolean? = lookup(key) as Boolean?

    private fun lookup(key: String) = key
        .splitToSequence('.')
        .fold<String, Any?>(document) { current, subkey ->
            (current as? Map<*, *>)?.get(subkey)
        }
}
