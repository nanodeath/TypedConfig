package dev.madetobuild.typedconfig.runtime.checks

object NonNegativeIntegerCheck : IntegerCheck {
    override fun invoke(value: Int, name: String) {
        require(value >= 0) { "$name cannot be negative" }
    }
}
