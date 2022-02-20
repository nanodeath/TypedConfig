package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName

internal data class ListDef(
    override val key: String,
    override val defaultValue: List<String>?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata,
    val genericType: ConfigDef<*>
) : ConfigDef<String> {
    override val type = List::class.asTypeName().parameterizedBy(genericType.type)
    override val keyClass = ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "ListKey" else "NullableListKey")
    override val templateString
        get() = "%T(%S, %N, $defaultTemplate, listOf(${constraints.joinToString(", ") { "%T" }})) { %T.parse(it) }"
    override val templateArgs: Array<Any?>
        get() = arrayOf(
            keyClass, key, "source", *defaultValueForTemplate, *constraints.toTypedArray(), genericType.keyClass
        )

    private val defaultTemplate: String
        get() =
            if (defaultValue == null) "%L"
            else "listOf(${defaultValue.joinToString(",") { "%S" }})"

    private val defaultValueForTemplate: Array<String?>
        get() = defaultValue?.toTypedArray() ?: arrayOf(null)
}