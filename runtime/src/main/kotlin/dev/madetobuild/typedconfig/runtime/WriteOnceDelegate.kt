package dev.madetobuild.typedconfig.runtime

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A delegate that supports having an initial value and then being updated **once**. Updating more than that will
 * throw an [IllegalStateException] with message [errorMessage].
 *
 * Thread-safe.
 */
internal class WriteOnceDelegate<T>(
    initialValue: T,
    private val errorMessage: String = "Property has already been written once"
) : ReadWriteProperty<Any, T> {
    @Volatile
    private var value: T = initialValue

    private var initialized = false

    override fun getValue(thisRef: Any, property: KProperty<*>): T = value

    @Synchronized
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        check(!initialized) { errorMessage }
        this.value = value
        initialized = true
    }
}
