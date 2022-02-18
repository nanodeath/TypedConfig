package com.github.nanodeath.typedconfig.runtime

import com.github.nanodeath.typedconfig.runtime.source.EnvSource
import com.github.nanodeath.typedconfig.runtime.source.Source

object TypedConfig {
    private var sourceInitialized = false

    var defaultSource: Source = EnvSource()
        @Synchronized get
        @Synchronized
        set(value) {
            if (!sourceInitialized) {
                field = value
                sourceInitialized = true
            } else {
                throw IllegalStateException("defaultSource has already been initialized!")
            }
        }
}
