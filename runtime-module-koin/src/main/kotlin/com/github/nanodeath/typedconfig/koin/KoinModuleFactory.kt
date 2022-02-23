package com.github.nanodeath.typedconfig.koin

import com.github.nanodeath.typedconfig.runtime.GeneratedConfig
import com.github.nanodeath.typedconfig.runtime.Key
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun GeneratedConfig.asKoinModule(): Module = module {
    this@asKoinModule.getAllKeys().forEach { prop ->
        val name = prop.findAnnotation<Key>()!!.name
        val type = prop.returnType
        if (type.isMarkedNullable) {
            // Uh-oh! Koin doesn't like these.
            // I guess we can bind an Optional though.
            factory(named(name)) { Optional.ofNullable(prop.get()) }
        } else {
            val clazz = type.classifier as KClass<Any>
            factory(named(name)) { prop.get()!! } bind clazz
        }
    }
}
