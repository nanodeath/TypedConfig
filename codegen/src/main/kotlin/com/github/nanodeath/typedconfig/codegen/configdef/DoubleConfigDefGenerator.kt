package com.github.nanodeath.typedconfig.codegen.configdef

import com.squareup.kotlinpoet.ClassName

internal object DoubleConfigDefGenerator : ConfigDefGenerator<DoubleConfigDef> {
    override val key = "double"

    override fun generate(
        key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
    ) = DoubleConfigDef(
        key, defaultValue?.toDoubleOrNull(), checks, metadata
    )
}
