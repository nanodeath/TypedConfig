package com.github.nanodeath.typedconfig.runtime.constraints

interface StringConstraint {
    operator fun invoke(value: String, name: String)
}
