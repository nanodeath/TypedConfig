package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.missingConfigurationExceptionName
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

interface ConfigDef<T> {
    val key: String
    val defaultValue: Any?
    val checks: List<ClassName>
    val type: TypeName
    val keyClass: ClassName
    val metadata: ConfigDefMetadata
    val templateString: String
    val templateArgs: Array<Any?>
}

val ConfigDef<*>.kdoc: CodeBlock
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
