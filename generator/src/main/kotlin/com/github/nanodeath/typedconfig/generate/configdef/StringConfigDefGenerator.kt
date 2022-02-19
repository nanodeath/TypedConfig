package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal object StringConfigDefGenerator : ConfigDefGenerator<StringConfigDef> {
    override val key = "str"

    override fun mapConstraint(constraint: String): ClassName = when (constraint) {
        "notblank" -> ClassName("$basePkg.constraints", "NotBlankStringConstraint")
        else -> super.mapConstraint(constraint)
    }

    override fun generate(
        key: String, defaultValue: String?, constraints: List<ClassName>, metadata: ConfigDefMetadata
    ) = StringConfigDef(
        key, defaultValue, constraints, required = metadata.required
    )
}
