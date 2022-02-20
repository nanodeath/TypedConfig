package com.github.nanodeath.typedconfig.codegen.configdef

import com.squareup.kotlinpoet.ClassName

internal object DurationConfigDefGenerator : ConfigDefGenerator<DurationConfigDef> {
    override val key = "duration"

    override fun generate(
        key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
    ) = DurationConfigDef(
        key, defaultValue, checks, metadata
    )
}
