package name.maxaller.konfigurator.runtime.source

import name.maxaller.konfigurator.runtime.camelToUpperSnake

object EnvSource : Source {
    override fun getInt(key: String): Int? = System.getenv(key.camelToUpperSnake())?.toIntOrNull()
    override fun getString(key: String): String? = System.getenv(key.camelToUpperSnake())
}
