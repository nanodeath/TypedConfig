package com.github.nanodeath.typedconfig.generate

import com.github.nanodeath.typedconfig.generate.configdef.ConfigDef
import com.github.nanodeath.typedconfig.generate.configdef.ConfigDefMetadata

internal data class ConfigSpec<T>(val configDef: ConfigDef<T>, val metadata: ConfigDefMetadata)
