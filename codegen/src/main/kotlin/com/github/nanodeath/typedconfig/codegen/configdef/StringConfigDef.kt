package com.github.nanodeath.typedconfig.codegen.configdef

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class StringConfigDef(
    override val key: String,
    override val defaultValue: String?,
    override val checks: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<String> {
    override val type = String::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "StringKey" else "NullableStringKey")

    override val templateString get() = "%T(%S, %N, %S, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )
}
