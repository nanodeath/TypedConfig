package name.maxaller.konfigurator.runtime.constraints

object NonNegativeConstraint : IntConstraint {
    override fun invoke(value: Int, name: String) {
        require(value >= 0) { "$name cannot be negative" }
    }
}
