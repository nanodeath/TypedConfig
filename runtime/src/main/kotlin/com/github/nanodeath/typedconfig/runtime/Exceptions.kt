package com.github.nanodeath.typedconfig.runtime

sealed class TypedConfigException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)

}

class MissingConfigurationException(key: String) :
    TypedConfigException("Config `$key` is required but could not be resolved and no default was provided")
