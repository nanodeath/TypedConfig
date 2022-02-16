package name.maxaller.konfigurator.runtime.source

import name.maxaller.konfigurator.runtime.camelToUpperSnake

class EnvSource internal constructor(private val env: Env) : Source {
    constructor() : this(Env())
    override fun getInt(key: String): Int? = env.get(key.camelToUpperSnake())?.toIntOrNull()
    override fun getString(key: String): String? = env.get(key.camelToUpperSnake())
}
