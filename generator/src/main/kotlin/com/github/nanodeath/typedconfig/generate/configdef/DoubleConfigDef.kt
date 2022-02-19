package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal data class DoubleConfigDef(
    override val key: String,
    override val defaultValue: Double?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Double> {
    override val type = Double::class
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "DoubleKey" else "NullableDoubleKey")
}
