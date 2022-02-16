package name.maxaller.konfigurator.runtime.source

interface Source {
    fun getInt(key: String): Int?
    fun getString(key: String): String?
    fun getDouble(key: String): Double?
}
