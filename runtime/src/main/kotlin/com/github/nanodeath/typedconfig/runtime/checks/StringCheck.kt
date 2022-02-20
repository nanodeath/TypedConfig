package com.github.nanodeath.typedconfig.runtime.checks

interface StringCheck {
    operator fun invoke(value: String, name: String)
}
