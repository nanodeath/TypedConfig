package dev.madetobuild.typedconfig.runtime.source

import dev.madetobuild.typedconfig.runtime.key.Key

interface Source {
    fun getInt(key: Key<*>): Int?
    fun getString(key: Key<*>): String?
    fun getDouble(key: Key<*>): Double?
    fun getBoolean(key: Key<*>): Boolean?
    fun getList(key: Key<*>): List<String>?
}
