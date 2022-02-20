package com.github.nanodeath.typedconfig.generate

import com.github.nanodeath.typedconfig.generate.configdef.ConfigDef
import com.github.nanodeath.typedconfig.generate.configdef.ConfigDefMetadata
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName

/**
 * Support class for [ConfigurationReader].
 */
internal class ConfigDefProperty(private val configDef: ConfigDef<*>) {
    private val metadata: ConfigDefMetadata = configDef.metadata

    val constraints: List<ClassName> = configDef.constraints
    val type: TypeName = configDef.type.copy(nullable = !metadata.required)
    val kdoc: CodeBlock = CodeBlock.builder().apply {
        // Add description if provided.
        if (!metadata.description.isNullOrBlank()) {
            addStatement(metadata.description)
        }
        // Add a comment for the default/optional-ness.
        addStatement(if (configDef.defaultValue != null) {
            "Default: ${configDef.defaultValue}"
        } else if (metadata.required) {
            "Required."
        } else {
            "Optional."
        })
        // Warn about exceptions it might throw.
        if (metadata.required && configDef.defaultValue == null) {
            addStatement("@throws %T", missingConfigurationExceptionName)
        }
    }.build()
}
