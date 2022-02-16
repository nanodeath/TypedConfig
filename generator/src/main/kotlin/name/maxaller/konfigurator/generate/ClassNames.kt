package name.maxaller.konfigurator.generate

import com.squareup.kotlinpoet.ClassName

private const val basePkg = "name.maxaller.konfigurator.runtime"
internal val sourceClassName = ClassName("$basePkg.source", "Source")
internal val nonNegativeIntClassName = ClassName("$basePkg.constraints", "NonNegativeIntConstraint")
internal val notBlankStringClassName = ClassName("$basePkg.constraints", "NotBlankStringConstraint")
internal val intKeyClassName = ClassName("$basePkg.key", "IntKey")
internal val stringKeyClassName = ClassName("$basePkg.key", "StringKey")
