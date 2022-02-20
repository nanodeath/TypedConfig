package com.github.nanodeath.typedconfig.runtime.checks

object NonNegativeIntCheck : IntCheck {
    override fun invoke(value: Int, name: String) {
        require(value >= 0) { "$name cannot be negative" }
    }
}
