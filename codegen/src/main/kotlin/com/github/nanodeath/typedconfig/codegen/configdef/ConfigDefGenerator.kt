package com.github.nanodeath.typedconfig.codegen.configdef

import com.squareup.kotlinpoet.ClassName

interface ConfigDefGenerator<T : ConfigDef<*>> {
    val type: String

    fun mapChecks(checkName: String): ClassName {
        // TODO replace "Unsupported check" with more specific exception
        throw IllegalArgumentException("Unsupported check: $checkName")
    }

    fun generate(key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata): T
}
