package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal data class StringConfigDef(
    override val key: String,
    override val defaultValue: String?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<String> {
    override val type = String::class
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "StringKey" else "NullableStringKey")
}
