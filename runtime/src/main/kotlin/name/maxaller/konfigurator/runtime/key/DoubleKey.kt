package name.maxaller.konfigurator.runtime.key

import name.maxaller.konfigurator.runtime.constraints.DoubleConstraint
import name.maxaller.konfigurator.runtime.source.Source

class DoubleKey(
    private val name: String,
    private val source: Source,
    private val default: Double,
    private val constraints: List<DoubleConstraint>
) : Key<Double> {
    override fun resolve(): Double {
        val value = source.getDouble(name) ?: default
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
