package com.github.nanodeath.typedconfig.runtime

@Suppress("unused")
sealed class TypedConfigException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Exception) : super(message, cause)
}

class MissingConfigurationException(key: String) :
    TypedConfigException("Config `$key` is required but could not be resolved and no default was provided")

class ParseException : TypedConfigException {
    constructor() : super()
    constructor(key: String, msg: String) : super("Failed to parse `$key`: $msg")
    constructor(key: String, msg: String, e: Exception) : super("Failed to parse `$key`: $msg", e)
}
