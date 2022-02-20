package com.github.nanodeath.typedconfig.codegen.configdef

import com.squareup.kotlinpoet.ClassName

interface ConfigDefGenerator<T : ConfigDef<*>> {
    val key: String

    fun mapChecks(check: String): ClassName {
        // TODO replace "Unsupported check" with more specific exception
        throw IllegalArgumentException("Unsupported check: $check")
    }

    fun generate(key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata): T
}

interface CollectionDefGenerator<T : ConfigDef<*>> {
    val key: String

    fun generate(key: String, defaultValue: List<String>?, metadata: ConfigDefMetadata, genericType: ConfigDef<*>): T
}
