package com.github.nanodeath.typedconfig.runtime.checks

interface DoubleCheck {
    operator fun invoke(value: Double, name: String)
}
