package name.maxaller.konfigurator.runtime

object EnvSource : Source {
    override fun getInt(key: String): Int? = System.getenv(key.camelToUpperSnake())?.toIntOrNull()
    override fun getString(key: String): String? = System.getenv(key.camelToUpperSnake())
}
