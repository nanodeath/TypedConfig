package dev.madetobuild.typedconfig

internal class Versions(pluginProperties: PluginProperties) {
    val codegenDependency = "dev.madetobuild:typedconfig-codegen:${pluginProperties.version}"
    val runtimeDependency = "dev.madetobuild:typedconfig-runtime:${pluginProperties.version}"
}
