package com.github.nanodeath.typedconfig

internal class Versions(pluginProperties: PluginProperties) {
    val codegenDependency = "com.github.nanodeath.typedconfig:codegen:${pluginProperties.version}"
    val runtimeDependency = "com.github.nanodeath.typedconfig:runtime:${pluginProperties.version}"
}
