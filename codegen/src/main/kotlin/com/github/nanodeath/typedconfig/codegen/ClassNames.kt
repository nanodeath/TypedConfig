package com.github.nanodeath.typedconfig.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName

internal const val RUNTIME_PACKAGE = "com.github.nanodeath.typedconfig.runtime"
internal val missingConfigurationExceptionName = ClassName(RUNTIME_PACKAGE, "MissingConfigurationException")

/**
 * <*> generic.
 */
internal val starType = WildcardTypeName.producerOf(
    Any::class.asTypeName().copy(nullable = true)
)
