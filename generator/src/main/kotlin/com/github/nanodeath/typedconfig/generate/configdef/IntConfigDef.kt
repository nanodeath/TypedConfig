package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class IntConfigDef(
    override val key: String,
    override val defaultValue: Int?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Int> {
    override val type = Int::class.asTypeName()
    override val keyClass = ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "IntKey" else "NullableIntKey")

    override val templateString get() = "%T(%S, %N, %L, listOf(${constraints.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *constraints.toTypedArray()
        )
}
