package com.github.nanodeath.typedconfig.generate.configdef

import com.github.nanodeath.typedconfig.generate.RUNTIME_PACKAGE
import com.squareup.kotlinpoet.ClassName
import java.time.Duration

internal data class DurationConfigDef(
    override val key: String,
    override val defaultValue: String?,
    override val constraints: List<ClassName>,
    override val metadata: ConfigDefMetadata
) : ConfigDef<Duration> {
    override val type = Duration::class
    override val keyClass =
        ClassName("$RUNTIME_PACKAGE.key", if (metadata.required) "DurationKey" else "NullableDurationKey")

    override val literalPlaceholder = "%S"
}
