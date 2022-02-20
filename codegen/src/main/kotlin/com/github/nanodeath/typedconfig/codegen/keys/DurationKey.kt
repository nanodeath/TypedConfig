package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import java.time.Duration

internal data class DurationKey(
    override val key: String,
    override val defaultValue: String?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata
) : Key<Duration> {
    override val type = Duration::class.asTypeName()
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "DurationKey" else "NullableDurationKey")

    override val templateString get() = "%T(%S, %N, %S, listOf(${checks.joinToString(", ") { "%T" }}))"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", defaultValue, *checks.toTypedArray()
        )

    internal object Generator : KeyGenerator<DurationKey> {
        override val type = "duration"

        override fun generate(
            key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata
        ) = DurationKey(
            key, defaultValue, checks, metadata
        )
    }
}
