package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal data class StringConfigDef(
    override val key: String,
    override val defaultValue: String?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<String> {
    override val type = String::class
    override val keyClass = ClassName("$basePkg.key", "${if (!metadata.required) "Nullable" else ""}StringKey")
}
