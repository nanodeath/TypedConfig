package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.appendKey

interface KeyObject<T> {
    fun parse(value: String): T
}

fun <T> KeyObject<T>.parseWithName(value: String, name: String) =
    try {
        parse(value)
    } catch (e: ParseException) {
        throw e.appendKey(name)
    }
