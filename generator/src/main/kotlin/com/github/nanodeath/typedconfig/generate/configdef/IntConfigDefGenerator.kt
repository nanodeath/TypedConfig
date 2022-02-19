package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal object IntConfigDefGenerator : ConfigDefGenerator<IntConfigDef> {
    override val key = "int"

    override fun mapConstraint(constraint: String): ClassName = when (constraint) {
        "nonnegative" -> ClassName("$RUNTIME_PACKAGE.constraints", "NonNegativeIntConstraint")
        else -> super.mapConstraint(constraint)
    }

    override fun generate(
        key: String, defaultValue: String?, constraints: List<ClassName>, metadata: ConfigDefMetadata
    ): IntConfigDef {
        // TODO better default value parsing here. Fail if not empty but also not an int.
        return IntConfigDef(
            key, defaultValue?.toIntOrNull(), constraints, metadata
        )
    }
}
