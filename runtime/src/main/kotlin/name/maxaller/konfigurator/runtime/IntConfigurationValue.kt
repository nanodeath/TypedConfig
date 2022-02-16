package name.maxaller.konfigurator.runtime

import name.maxaller.konfigurator.runtime.constraints.IntConstraint
import name.maxaller.konfigurator.runtime.source.Source

class IntConfigurationValue(
    private val name: String,
    private val source: Source,
    private val default: Int,
    private val constraints: List<IntConstraint>
) : ConfigurationValue<Int> {
    override fun resolve(): Int {
        val value = source.getInt(name) ?: default
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
