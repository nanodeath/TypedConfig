package com.github.nanodeath.typedconfig.generate

internal data class IntConfigDef(
    val key: String,
    val defaultValue: Int?,
    val constraints: List<String>
) : ConfigDef
