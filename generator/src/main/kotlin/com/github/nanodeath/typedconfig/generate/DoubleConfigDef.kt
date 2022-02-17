package com.github.nanodeath.typedconfig.generate

internal data class DoubleConfigDef(
    val key: String,
    val defaultValue: Double?,
    val constraints: List<String>
) : ConfigDef
