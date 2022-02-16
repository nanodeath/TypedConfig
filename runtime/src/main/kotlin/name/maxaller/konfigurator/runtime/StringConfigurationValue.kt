package name.maxaller.konfigurator.runtime

import name.maxaller.konfigurator.runtime.constraints.StringConstraint
import name.maxaller.konfigurator.runtime.source.Source

class StringConfigurationValue(
    private val name: String,
    private val source: Source,
    private val default: String,
    private val constraints: List<StringConstraint>
) : ConfigurationValue<String> {
    override fun resolve(): String {
        val value = source.getString(name) ?: default
        if (constraints.isNotEmpty()) {
            for (constraint in constraints) {
                constraint(value, name)
            }
        }
        return value
    }
}
