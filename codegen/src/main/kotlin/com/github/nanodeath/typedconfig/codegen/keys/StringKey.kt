package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class StringKey(
    override val key: String,
    override val defaultValue: String?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata
) : Key<String> {
    override val type = String::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "StringKey" else "NullableStringKey")
    override val supportsSensitiveFlag = true
    override val templateString get() = "%T(%S, %N, %S, listOf(${checks.joinToString(", ") { "%T" }}), %L)"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray(), metadata.sensitive
        )

    internal object Generator : KeyGenerator<StringKey> {
        override val type = "str"

        override fun mapChecks(checkName: String): ClassName = when (checkName) {
            "notblank" -> ClassName("$RUNTIME_PACKAGE.checks", "NotBlankStringCheck")
            else -> super.mapChecks(checkName)
        }

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata
        ) = StringKey(
            key, defaultValue, checks, metadata
        )
    }
}
