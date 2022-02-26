package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.appendKey

interface KeyObject<T> {
    fun parse(value: String): T
}

fun <T> KeyObject<T>.parseWithName(value: String, name: String) =
    try {
        parse(value)
    } catch (e: ParseException) {
        throw e.appendKey(name)
    }
