package com.github.nanodeath.typedconfig.codegen.keys

import com.github.nanodeath.typedconfig.codegen.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName

internal data class SetKey(
    override val key: String,
    override val defaultValue: List<String>?,
    override val checks: List<ClassName>,
    override val metadata: KeyMetadata,
    val genericType: Key<*>
) : Key<String> {
    private val listKey = ListKey(key, defaultValue, checks, metadata, genericType)

    override val type = Set::class.asTypeName().parameterizedBy(genericType.type)
    override val keyClass = listKey.keyClass
    override val templateString
        get() = listKey.templateString + ".%M()"
    override val templateArgs
        get() = listKey.templateArgs + MemberName("$RUNTIME_PACKAGE.key", "asSet")

    internal object Generator : CollectionKeyGenerator<SetKey> {
        override val type = "set"

        override fun generate(
            key: String,
            defaultValue: List<String>?,
            metadata: KeyMetadata,
            genericKeyType: Key<*>
        ) = SetKey(key, defaultValue, emptyList(), metadata, genericKeyType)
    }
}
