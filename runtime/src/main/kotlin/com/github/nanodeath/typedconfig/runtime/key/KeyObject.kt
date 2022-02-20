package com.github.nanodeath.typedconfig.runtime.key

interface KeyObject<T> {
    fun parse(value: String): T
}
