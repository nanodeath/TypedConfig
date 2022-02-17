package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal object IntConfigDefGenerator : ConfigDefGenerator<IntConfigDef> {
    override val key = "int"

    override fun mapConstraint(constraint: String): ClassName =
        when (constraint) {
            "nonnegative" -> ClassName("$basePkg.constraints", "NonNegativeIntConstraint")
            else -> super.mapConstraint(constraint)
        }

    override fun generate(key: String, defaultValue: String?, constraints: List<ClassName>): IntConfigDef {
        // TODO better default value parsing here. Fail if not empty but also not an int.
        return IntConfigDef(key, defaultValue?.toIntOrNull(), constraints)
    }
}
