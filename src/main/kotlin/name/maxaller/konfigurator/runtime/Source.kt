package name.maxaller.konfigurator.runtime

interface Source {
    fun getInt(key: String): Int?
    fun getString(key: String): String?
}
