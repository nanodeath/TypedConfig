package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal object IntConfigDefGenerator : ConfigDefGenerator<IntConfigDef> {
    override val key = "int"

    override fun mapChecks(check: String): ClassName = when (check) {
        "nonnegative" -> ClassName("$RUNTIME_PACKAGE.checks", "NonNegativeIntCheck")
        else -> super.mapChecks(check)
    }

    override fun generate(
        key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
    ): IntConfigDef {
        // TODO better default value parsing here. Fail if not empty but also not an int.
        return IntConfigDef(
            key, defaultValue?.toIntOrNull(), checks, metadata
        )
    }
}
