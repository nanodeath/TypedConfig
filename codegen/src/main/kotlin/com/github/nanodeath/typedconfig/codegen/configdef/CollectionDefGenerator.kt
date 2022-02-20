package com.github.nanodeath.typedconfig.codegen.configdef

interface CollectionDefGenerator<T : ConfigDef<*>> {
    val key: String

    fun generate(key: String, defaultValue: List<String>?, metadata: ConfigDefMetadata, genericType: ConfigDef<*>): T
}
