@file:Suppress("unused")

import com.example.GeneratedConfig
import com.github.nanodeath.typedconfig.runtime.TypedConfig
import com.github.nanodeath.typedconfig.runtime.source.EnvSource

fun main() {
    // This is the conventional way to instantiate a configuration object.
    // Other options are below.
    val config = GeneratedConfig.default()
    val greeting = config.greeting
    println(greeting)
    println("Configuration was compiled and loaded successfully!")
    println("If you run this application with the GREETING environment variable set,")
    println("that will be printed instead.")
}

private fun settingDefaultSource(): GeneratedConfig {
    TypedConfig.defaultSource = EnvSource() // this is what you get if you don't set a default source anyway
    return GeneratedConfig.default()
}

private fun notUsingDefaultSource(): GeneratedConfig {
    // You can also use the constructor if you need.
    return GeneratedConfig(EnvSource())
}
