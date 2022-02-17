package com.github.nanodeath.typedconfig.runtime.constraints

interface DoubleConstraint {
    operator fun invoke(value: Double, name: String)
}
