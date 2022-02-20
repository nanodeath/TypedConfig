package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class BooleanConfigDef(
    override val key: String,
    override val defaultValue: Boolean?,
    override val checks: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Boolean> {
    override val type = Boolean::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "BooleanKey" else "NullableBooleanKey")
    override val templateString get() = "%T(%S, %N, %L, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )
}
