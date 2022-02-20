package com.github.nanodeath.typedconfig.runtime.checks

interface IntCheck {
    operator fun invoke(value: Int, name: String)
}
