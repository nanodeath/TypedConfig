package com.github.nanodeath.typedconfig.runtime.constraints

interface IntConstraint {
    operator fun invoke(value: Int, name: String)
}
