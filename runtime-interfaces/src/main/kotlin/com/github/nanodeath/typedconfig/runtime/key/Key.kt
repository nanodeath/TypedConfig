package com.github.nanodeath.typedconfig.runtime.key

import kotlin.reflect.KProperty

interface Key<T> {
    val name: String

    fun resolve(): T
    operator fun getValue(generatedConfig: Any?, property: KProperty<*>): T = resolve()
}
