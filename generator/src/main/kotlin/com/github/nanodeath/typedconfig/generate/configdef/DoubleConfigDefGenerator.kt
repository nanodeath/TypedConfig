package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName

internal object DoubleConfigDefGenerator : ConfigDefGenerator<DoubleConfigDef> {
    override val key = "double"

    override fun generate(key: String, defaultValue: String?, constraints: List<ClassName>) =
        DoubleConfigDef(
            key,
            defaultValue?.toDoubleOrNull(),
            constraints
        )
}