package com.github.nanodeath.typedconfig.generate

import com.squareup.kotlinpoet.ClassName

private const val basePkg = "com.github.nanodeath.typedconfig.runtime"
internal val sourceClassName = ClassName("$basePkg.source", "Source")
internal val nonNegativeIntClassName = ClassName("$basePkg.constraints", "NonNegativeIntConstraint")
internal val notBlankStringClassName = ClassName("$basePkg.constraints", "NotBlankStringConstraint")
internal val intKeyClassName = ClassName("$basePkg.key", "IntKey")
internal val stringKeyClassName = ClassName("$basePkg.key", "StringKey")
internal val doubleKeyClassName = ClassName("$basePkg.key", "DoubleKey")