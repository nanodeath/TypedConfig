package com.github.nanodeath.typedconfig.runtime.source

class MultiSource(private val sources: List<Source>): Source {
    override fun getInt(key: String): Int? = sources.firstNotNullOfOrNull { it.getInt(key) }
    override fun getString(key: String): String? = sources.firstNotNullOfOrNull { it.getString(key) }
    override fun getDouble(key: String): Double? = sources.firstNotNullOfOrNull { it.getDouble(key) }
    override fun getBoolean(key: String): Boolean? = sources.firstNotNullOfOrNull { it.getBoolean(key) }
    override fun getList(key: String): List<String>? = sources.firstNotNullOfOrNull { it.getList(key) }
}
