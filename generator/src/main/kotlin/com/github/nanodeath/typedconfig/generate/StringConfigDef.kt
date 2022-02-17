package com.github.nanodeath.typedconfig.generate

internal data class StringConfigDef(
    val key: String,
    val defaultValue: String?,
    val constraints: List<String>
) : ConfigDef
