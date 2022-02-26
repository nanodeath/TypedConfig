package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.MissingConfigurationException
import dev.madetobuild.typedconfig.runtime.checks.StringCheck
import dev.madetobuild.typedconfig.runtime.source.Source

class StringKey(
    override val name: String,
    private val source: Source,
    default: String?,
    private val checks: List<StringCheck>,
    override val sensitive: Boolean
) : Key<String> {
    // this obviously doesn't do much; it's just for consistency with the other Keys.
    private val parsedDefault: String? = default?.let { parseWithName(it, name) }

    override fun resolve(): String {
        val value = source.getString(this) ?: parsedDefault ?: throw MissingConfigurationException(name)
        if (checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }

    companion object: KeyObject<String> {
        override fun parse(value: String): String = value
    }
}
