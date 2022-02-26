package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.checks.StringCheck
import dev.madetobuild.typedconfig.runtime.source.Source

class NullableStringKey(
    override val name: String,
    private val source: Source,
    @Suppress("unused") private val default: Unit?,
    private val checks: List<StringCheck>,
    override val sensitive: Boolean
) : Key<String?> {
    override fun resolve(): String? {
        val value = source.getString(this)
        if (value != null && checks.isNotEmpty()) {
            for (check in checks) {
                check(value, name)
            }
        }
        return value
    }
}
