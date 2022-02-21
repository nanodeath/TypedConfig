package com.github.nanodeath.typedconfig

import java.util.Properties

class PluginProperties(map: Map<Any, Any>) {
    val version: String by map

    companion object {
        fun fromPropertiesFile(): PluginProperties {
            val props = Properties()
            val propertiesFile = checkNotNull(PluginProperties::class.java.getResourceAsStream("plugin.properties"))
            propertiesFile.use { props.load(it) }
            return PluginProperties(props)
        }
    }
}
