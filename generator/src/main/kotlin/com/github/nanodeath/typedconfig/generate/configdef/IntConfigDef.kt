package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.basePkg
import com.squareup.kotlinpoet.ClassName

internal data class IntConfigDef(
    override val key: String,
    override val defaultValue: Int?,
    override val constraints: List<ClassName>
) : ConfigDef<Int> {
    override val type = Int::class
    override val keyClass = ClassName("$basePkg.key", "IntKey")
}
