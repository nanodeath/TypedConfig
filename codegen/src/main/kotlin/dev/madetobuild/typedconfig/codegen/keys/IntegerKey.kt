package dev.madetobuild.typedconfig.codegen.keys

import dev.madetobuild.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

internal data class IntegerKey(
    override val key: String,
    override val defaultValue: String?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata
) : Key<Int> {
    override val type = Int::class.asTypeName()
    override val keyClass = ClassName(
        "$RUNTIME_PACKAGE.key",
        if (metadata.required) "IntegerKey" else "NullableIntegerKey"
    )

    override val templateString get() = "%T(%S, %N, %S, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )

    internal object Generator : KeyGenerator<IntegerKey> {
        override val type = "int"

        override fun mapChecks(checkName: String): ClassName = when (checkName) {
            "nonnegative" -> ClassName("$RUNTIME_PACKAGE.checks", "NonNegativeIntegerCheck")
            else -> super.mapChecks(checkName)
        }

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata
        ) = IntegerKey(
            key, defaultValue, checks, metadata
        )
    }
}
