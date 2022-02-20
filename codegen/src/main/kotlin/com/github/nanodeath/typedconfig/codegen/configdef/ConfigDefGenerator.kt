package com.github.nanodeath.typedconfig.codegen.configdef

import com.squareup.kotlinpoet.ClassName

interface ConfigDefGenerator<T : ConfigDef<*>> {
    val type: String

    fun mapChecks(check: String): ClassName {
        // TODO replace "Unsupported check" with more specific exception
        throw IllegalArgumentException("Unsupported check: $check")
    }

    fun generate(key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata): T
}
