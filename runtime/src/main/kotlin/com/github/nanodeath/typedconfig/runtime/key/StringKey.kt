package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.constraints.StringConstraint
import com.github.nanodeath.typedconfig.runtime.source.Source

class StringKey(
    private val name: String,
    private val source: Source,
    private val default: String?,
    private val constraints: List<StringConstraint>
) : Key<String> {
    override fun resolve(): String {
        val value = source.getString(name) ?: default ?: throw MissingConfigurationException(name)
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
