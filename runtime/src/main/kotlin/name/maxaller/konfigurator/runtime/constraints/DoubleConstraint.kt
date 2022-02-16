package name.maxaller.konfigurator.runtime.constraints

interface DoubleConstraint {
    operator fun invoke(value: Double, name: String)
}
