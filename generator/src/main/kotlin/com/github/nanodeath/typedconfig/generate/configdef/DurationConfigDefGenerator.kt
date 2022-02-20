package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName

internal object DurationConfigDefGenerator : ConfigDefGenerator<DurationConfigDef> {
    override val key = "duration"

    override fun generate(
        key: String, defaultValue: String?, constraints: List<ClassName>, metadata: ConfigDefMetadata
    ) = DurationConfigDef(
        key, defaultValue, constraints, metadata
    )
}
