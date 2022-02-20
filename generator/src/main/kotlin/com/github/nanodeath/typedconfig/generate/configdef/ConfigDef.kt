package com.github.nanodeath.typedconfig.generate.configdef

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

interface ConfigDef<T> {
    val key: String
    val defaultValue: Any?
    val constraints: List<ClassName>
    val type: TypeName
    val keyClass: ClassName
    val metadata: ConfigDefMetadata
    val templateString: String
    val templateArgs: Array<Any?>
}
