package com.github.nanodeath.typedconfig.codegen.keys

interface CollectionKeyGenerator<T : Key<*>> {
    val type: String

    fun generate(key: String, defaultValue: List<String>?, metadata: KeyMetadata, genericKeyType: Key<*>): T
}
