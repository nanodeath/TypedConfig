package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.source.Source

class ListKey<T>(
    private val name: String,
    private val source: Source,
    private val default: List<String>?,
    @Suppress("unused") private val constraints: List<Unit>,
    private val parse: (String) -> T
) : Key<List<T>> {
    override fun resolve(): List<T> =
        (source.getList(name) ?: default ?: throw MissingConfigurationException(name)).map(parse)
}
