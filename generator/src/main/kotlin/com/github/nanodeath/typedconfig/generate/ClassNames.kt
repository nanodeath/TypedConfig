package com.github.nanodeath.typedconfig.generate

import com.squareup.kotlinpoet.ClassName

internal const val basePkg = "com.github.nanodeath.typedconfig.runtime"
internal val missingConfigurationExceptionName = ClassName(basePkg, "MissingConfigurationException")
