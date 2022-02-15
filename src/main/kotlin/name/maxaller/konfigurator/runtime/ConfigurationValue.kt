package name.maxaller.konfigurator.runtime

import kotlin.reflect.KProperty

interface ConfigurationValue<T> {
    fun resolve(): T
    operator fun getValue(generatedConfig: Any?, property: KProperty<*>): T = resolve()
}
