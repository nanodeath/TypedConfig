package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class BooleanKey(
    override val key: String,
    override val defaultValue: Boolean?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata
) : Key<Boolean> {
    override val type = Boolean::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "BooleanKey" else "NullableBooleanKey")
    override val templateString get() = "%T(%S, %N, %L, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )

    internal object Generator : KeyGenerator<BooleanKey> {
        override val type = "bool"

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata
        ) = BooleanKey(
            key, defaultValue?.toBoolean(), checks, metadata
        )
    }
}
