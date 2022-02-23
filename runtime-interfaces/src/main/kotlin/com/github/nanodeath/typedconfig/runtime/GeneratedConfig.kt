package com.github.nanodeath.typedconfig.runtime

import kotlin.reflect.KProperty0

interface GeneratedConfig {
    fun getAllKeys(): Sequence<KProperty0<*>>
}
