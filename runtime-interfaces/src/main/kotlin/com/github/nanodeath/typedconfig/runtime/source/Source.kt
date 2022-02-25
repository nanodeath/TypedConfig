package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.key.Key

interface Source {
    fun getInt(key: Key<*>): Int?
    fun getString(key: Key<*>): String?
    fun getDouble(key: Key<*>): Double?
    fun getBoolean(key: Key<*>): Boolean?
    fun getList(key: Key<*>): List<String>?
}
