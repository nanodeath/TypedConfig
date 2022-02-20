package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName

internal object StringConfigDefGenerator : ConfigDefGenerator<StringConfigDef> {
    override val key = "str"

    override fun mapChecks(check: String): ClassName = when (check) {
        "notblank" -> ClassName("$RUNTIME_PACKAGE.checks", "NotBlankStringCheck")
        else -> super.mapChecks(check)
    }

    override fun generate(
        key: String, defaultValue: String?, checks: List<ClassName>, metadata: ConfigDefMetadata
    ) = StringConfigDef(
        key, defaultValue, checks, metadata
    )
}
