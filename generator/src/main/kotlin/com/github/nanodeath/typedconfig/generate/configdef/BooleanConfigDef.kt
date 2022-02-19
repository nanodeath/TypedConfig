package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal data class BooleanConfigDef(
    override val key: String,
    override val defaultValue: Boolean?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Boolean> {
    override val type = Boolean::class
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "BooleanKey" else "NullableBooleanKey")
}
