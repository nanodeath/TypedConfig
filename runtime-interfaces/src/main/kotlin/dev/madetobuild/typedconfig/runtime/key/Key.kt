package dev.madetobuild.typedconfig.runtime.key

import kotlin.reflect.KProperty

interface Key<T> {
    val name: String
    val sensitive: Boolean get() = false

    fun resolve(): T
    operator fun getValue(generatedConfig: Any?, property: KProperty<*>): T = resolve()
}

fun Key<*>.maskIfSensitive(value: String?): String? = if (sensitive) "[*****]" else value
