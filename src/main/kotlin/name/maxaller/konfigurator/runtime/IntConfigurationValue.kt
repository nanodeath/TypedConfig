package name.maxaller.konfigurator.runtime

import name.maxaller.konfigurator.runtime.constraints.IntConstraint
import kotlin.reflect.KProperty

class IntConfigurationValue(
    private val source: Source,
    private val default: Int,
    private val constraints: List<IntConstraint>
) {
    operator fun getValue(generatedConfig: Any?, property: KProperty<*>): Int {
        val name = property.name
        val value = source.getInt(name) ?: default
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
