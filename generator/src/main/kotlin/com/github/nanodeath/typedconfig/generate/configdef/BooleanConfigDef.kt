package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal data class BooleanConfigDef(
    override val key: String,
    override val defaultValue: Boolean?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Boolean> {
    override val type = Boolean::class
    override val keyClass = ClassName("$basePkg.key", if (metadata.required) "BooleanKey" else "NullableBooleanKey")
}
