package com.github.nanodeath.typedconfig.runtime

@Suppress("unused")
sealed class TypedConfigException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}

class MissingConfigurationException(key: String) :
    TypedConfigException("Config `$key` is required but could not be resolved and no default was provided")

class ParseException(key: String, msg: String) : TypedConfigException("Failed to parse `$key`: $msg")
