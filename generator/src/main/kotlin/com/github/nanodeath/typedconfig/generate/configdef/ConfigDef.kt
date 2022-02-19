package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName
import kotlin.reflect.KClass

interface ConfigDef<T> {
    val key: String
    val defaultValue: T?
    val constraints: List<ClassName>
    val type: KClass<*>
    val keyClass: ClassName
    val metadata: ConfigDefMetadata
    val literalPlaceholder: String get() = "%L"
}
