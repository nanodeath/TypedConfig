package com.github.nanodeath.typedconfig.runtime

import com.github.nanodeath.typedconfig.runtime.source.EnvSource
import com.github.nanodeath.typedconfig.runtime.source.Source

object TypedConfig {
    var defaultSource: Source by WriteOnceDelegate(EnvSource(), "defaultSource has already been initialized")
}
