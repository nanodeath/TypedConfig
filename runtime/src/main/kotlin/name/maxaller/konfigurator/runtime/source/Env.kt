package name.maxaller.konfigurator.runtime.source

internal class Env {
    fun get(key: String): String? = System.getenv(key)
}
