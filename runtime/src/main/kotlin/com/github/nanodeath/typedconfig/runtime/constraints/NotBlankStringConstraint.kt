package com.github.nanodeath.typedconfig.runtime.constraints

object NotBlankStringConstraint : StringConstraint {
    override fun invoke(value: String, name: String) {
        require(value.isNotBlank()) { "$name cannot be blank" }
    }
}
