package com.github.nanodeath.typedconfig.codegen.configdef

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class DoubleConfigDef(
    override val key: String,
    override val defaultValue: Double?,
    override val checks: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Double> {
    override val type = Double::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "DoubleKey" else "NullableDoubleKey")

    override val templateString get() = "%T(%S, %N, %L, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )
}
