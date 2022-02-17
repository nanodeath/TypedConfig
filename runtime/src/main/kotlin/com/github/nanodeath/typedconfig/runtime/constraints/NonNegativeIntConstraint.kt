package com.github.nanodeath.typedconfig.runtime.constraints

object NonNegativeIntConstraint : IntConstraint {
    override fun invoke(value: Int, name: String) {
        require(value >= 0) { "$name cannot be negative" }
    }
}
