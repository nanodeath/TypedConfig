package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName

internal object BooleanConfigDefGenerator : ConfigDefGenerator<BooleanConfigDef> {
    override val key = "bool"

    override fun generate(
        key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
    ) = BooleanConfigDef(
        key, defaultValue?.toBoolean(), checks, metadata
    )
}
