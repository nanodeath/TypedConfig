package com.github.nanodeath.typedconfig

internal object Versions {
    private const val typedConfigVersion = "@VERSION@"
    val codegenDependency = "com.github.nanodeath.typedconfig:codegen:$typedConfigVersion"
    val runtimeDependency = "com.github.nanodeath.typedconfig:runtime:$typedConfigVersion"
}
