package com.github.nanodeath.typedconfig.codegen.configdef

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class IntConfigDef(
    override val key: String,
    override val defaultValue: Int?,
    override val checks: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Int> {
    override val type = Int::class.asTypeName()
    override val keyClass = ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "IntKey" else "NullableIntKey")

    override val templateString get() = "%T(%S, %N, %L, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )

    internal object Generator : ConfigDefGenerator<IntConfigDef> {
        override val type = "int"

        override fun mapChecks(check: String): ClassName = when (check) {
            "nonnegative" -> ClassName("$RUNTIME_PACKAGE.checks", "NonNegativeIntCheck")
            else -> super.mapChecks(check)
        }

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
        ): IntConfigDef {
            // TODO better default value parsing here. Fail if not empty but also not an int.
            return IntConfigDef(
                key, defaultValue?.toIntOrNull(), checks, metadata
            )
        }
    }
}
