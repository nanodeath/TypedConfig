package dev.madetobuild.typedconfig.runtime

import dev.madetobuild.typedconfig.runtime.source.EnvSource
import dev.madetobuild.typedconfig.runtime.source.Source

object TypedConfig {
    var defaultSource: Source by WriteOnceDelegate(EnvSource(), "defaultSource has already been initialized")
}
