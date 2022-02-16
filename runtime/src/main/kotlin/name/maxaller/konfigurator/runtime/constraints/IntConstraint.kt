package name.maxaller.konfigurator.runtime.constraints

interface IntConstraint {
    operator fun invoke(value: Int, name: String)
}
