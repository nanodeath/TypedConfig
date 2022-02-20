package com.github.nanodeath.typedconfig.runtime.checks

object NotBlankStringCheck : StringCheck {
    override fun invoke(value: String, name: String) {
        require(value.isNotBlank()) { "$name cannot be blank" }
    }
}
