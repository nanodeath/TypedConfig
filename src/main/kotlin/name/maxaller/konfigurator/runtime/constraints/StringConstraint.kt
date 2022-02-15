package name.maxaller.konfigurator.runtime.constraints

interface StringConstraint {
    operator fun invoke(value: String, name: String)
}
