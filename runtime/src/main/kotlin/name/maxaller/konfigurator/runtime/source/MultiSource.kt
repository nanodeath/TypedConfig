package name.maxaller.konfigurator.runtime.source

class MultiSource(private val sources: List<Source>): Source {
    override fun getInt(key: String): Int? = sources.firstNotNullOfOrNull { it.getInt(key) }
    override fun getString(key: String): String? = sources.firstNotNullOfOrNull { it.getString(key) }
}
