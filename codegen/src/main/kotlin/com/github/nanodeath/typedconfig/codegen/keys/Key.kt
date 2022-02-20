package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.missingConfigurationExceptionName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

interface Key<T> {
    val key: String
    val defaultValue: Any?
    val checks: List<ClassName>
    val type: TypeName
    val keyClass: ClassName
    val metadata: KeyMetadata
    val templateString: String
    val templateArgs: Array<Any?>
}

val Key<*>.kdoc: CodeBlock
    get() = CodeBlock.builder().apply {
        // Add description if provided.
        val description = metadata.description
        if (!description.isNullOrBlank()) {
            addStatement(description)
        }
        // Add a comment for the default/optional-ness.
        addStatement(
            if (defaultValue != null) {
                "Default: $defaultValue"
            } else if (metadata.required) {
                "Required."
            } else {
                "Optional."
            }
        )
        // Warn about exceptions it might throw.
        if (metadata.required && defaultValue == null) {
            addStatement("@throws %T", missingConfigurationExceptionName)
        }
    }.build()
