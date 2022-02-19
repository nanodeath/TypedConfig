package com.github.nanodeath.typedconfig.generate

import com.squareup.kotlinpoet.ClassName

internal const val RUNTIME_PACKAGE = "com.github.nanodeath.typedconfig.runtime"
internal val missingConfigurationExceptionName = ClassName(RUNTIME_PACKAGE, "MissingConfigurationException")
