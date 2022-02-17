package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName

interface ConfigDefGenerator<T : ConfigDef<*>> {
    val key: String

    fun mapConstraint(constraint: String): ClassName {
        // TODO replace "Unsupported constraint" with more specific exception
        throw IllegalArgumentException("Unsupported constraint: $constraint")
    }

    fun generate(key: String, defaultValue: String?, constraints: List<ClassName>): T
}
