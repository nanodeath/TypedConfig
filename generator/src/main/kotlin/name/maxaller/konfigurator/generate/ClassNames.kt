package name.maxaller.konfigurator.generate

import com.squareup.kotlinpoet.ClassName

private const val basePkg = "name.maxaller.konfigurator.runtime"
internal val sourceClassName = ClassName("$basePkg.source", "Source")
internal val nonNegativeConstraintClassName =
    ClassName("$basePkg.constraints", "NonNegativeConstraint")
internal val intConfigurationValueClassName = ClassName(basePkg, "IntConfigurationValue")
internal val stringConfigurationValueClassName = ClassName(basePkg, "StringConfigurationValue")
