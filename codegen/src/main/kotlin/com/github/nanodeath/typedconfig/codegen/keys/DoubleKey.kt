package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class DoubleKey(
    override val key: String,
    override val defaultValue: String?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata
) : Key<Double> {
    override val type = Double::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "DoubleKey" else "NullableDoubleKey")

    override val templateString get() = "%T(%S, %N, %S, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )

    internal object Generator : KeyGenerator<DoubleKey> {
        override val type = "double"

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata
        ) = DoubleKey(
            key, defaultValue, checks, metadata
        )
    }
}
