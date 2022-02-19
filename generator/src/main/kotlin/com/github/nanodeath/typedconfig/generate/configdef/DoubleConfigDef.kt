package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal data class DoubleConfigDef(
    override val key: String,
    override val defaultValue: Double?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Double> {
    override val type = Double::class
    override val keyClass = ClassName("$basePkg.key", "${if (!metadata.required) "Nullable" else ""}DoubleKey")
}
