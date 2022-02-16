package name.maxaller.konfigurator.runtime.constraints

object NotBlankStringConstraint : StringConstraint {
    override fun invoke(value: String, name: String) {
        require(value.isNotBlank()) { "$name cannot be blank" }
    }
}
