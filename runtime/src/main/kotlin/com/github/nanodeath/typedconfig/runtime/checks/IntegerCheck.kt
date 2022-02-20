package com.github.nanodeath.typedconfig.runtime.checks

interface IntegerCheck {
    operator fun invoke(value: Int, name: String)
}
