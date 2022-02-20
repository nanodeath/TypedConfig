package com.github.nanodeath.typedconfig.generate.configdef


internal object ListDefGenerator : CollectionDefGenerator<ListDef> {
    override val key = "list"

    override fun generate(
        key: String,
        defaultValue: List<String>?,
        metadata: ConfigDefMetadata,
        genericType: ConfigDef<*>
    ): ListDef {
        return ListDef(key, defaultValue, emptyList(), metadata, genericType)
    }
}
