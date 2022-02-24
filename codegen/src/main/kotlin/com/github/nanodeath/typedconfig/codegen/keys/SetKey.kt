package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName

internal data class SetKey(
    override val key: String,
    override val defaultValue: List<String>?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata,
    val genericType: Key<*>
) : Key<String> {
    override val type = Set::class.asTypeName().parameterizedBy(genericType.type)
    override val keyClass = ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "SetKey" else "NullableSetKey")
    override val templateString
        get() = "%T(%S, %N, $defaultTemplate, listOf(${checks.joinToString(", ") { "%T" }})) { %T.parse(it) }"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", *defaultValueForTemplate, *checks.toTypedArray(), genericType.keyClass
        )

    private val defaultTemplate: String
        get() =
            if (defaultValue == null) "%L"
            else "setOf(${defaultValue.joinToString(",") { "%S" }})"

    private val defaultValueForTemplate: Array<String?>
        get() = defaultValue?.toTypedArray() ?: arrayOf(null)

    internal object Generator : CollectionKeyGenerator<SetKey> {
        override val type = "set"

        override fun generate(
            key: String,
            defaultValue: List<String>?,
            metadata: KeyMetadata,
            genericKeyType: Key<*>
        ) = SetKey(key, defaultValue, emptyList(), metadata, genericKeyType)
    }
}
