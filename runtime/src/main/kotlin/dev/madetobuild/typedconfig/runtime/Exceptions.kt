package dev.madetobuild.typedconfig.runtime

import dev.madetobuild.typedconfig.runtime.key.Key

@Suppress("unused")
sealed class TypedConfigException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
}

class MissingConfigurationException(key: String) :
    TypedConfigException("Config `$key` is required but could not be resolved and no default was provided")

class ParseException(val key: String?, msg: String, cause: Throwable?) : TypedConfigException(msg, cause) {
    constructor(msg: String) : this(null, msg, null)
}

fun ParseException.appendKey(key: String): ParseException {
    check(this.key == null) { "Key already set! Was ${this.key}, proposing $key" }
    return ParseException(key, "Parse failure on key `$key`: ${this.message}", this.cause).also { newException ->
        newException.stackTrace = this.stackTrace
    }
}

fun ParseException.appendKey(key: Key<*>): ParseException = this.appendKey(key.name)
