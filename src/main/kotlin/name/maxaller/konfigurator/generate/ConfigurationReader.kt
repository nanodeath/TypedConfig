package name.maxaller.konfigurator.generate

import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.squareup.kotlinpoet.*
import name.maxaller.konfigurator.runtime.IntConfigurationValue
import name.maxaller.konfigurator.runtime.Source
import name.maxaller.konfigurator.runtime.StringConfigurationValue
import name.maxaller.konfigurator.runtime.constraints.NonNegativeConstraint
import java.io.File

class ConfigurationReader {
    private val tomlMapper = TomlMapper()

    fun readFile(file: File): FileSpec {
        val node = tomlMapper.readTree(file)
        val configs = mutableListOf<ConfigDef>()
        for ((key, value) in node.fields().asSequence().filter { (_, v) -> v.isObject }) {
            require(key.isNotBlank()) { "Key cannot be empty or blank, was `${key}` in $file" }
            when (val datatype = value.path("datatype").textValue()) {
                "int" -> {
                    val defaultValue: Int? = value.get("default")?.intValue()
                    val constraints: List<String>? = (value.get("constraints") as ArrayNode?)?.map { it.textValue() }

                    configs.add(IntConfigDef(key, defaultValue, constraints.orEmpty()))
                }
                "str" -> {
                    val defaultValue: String? = value.get("default")?.textValue()
                    val constraints: List<String>? = (value.get("constraints") as ArrayNode?)?.map { it.textValue() }

                    configs.add(StringConfigDef(key, defaultValue, constraints.orEmpty()))
                }
                else -> throw IllegalArgumentException("Unsupported datatype: `${datatype}` in $file")
            }
        }

        val className = node.get("class").textValue()
        val configClass = TypeSpec.classBuilder(className)
        configClass.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("source", Source::class)
                .build()
        )
        configClass.addProperty(
            PropertySpec.builder("source", Source::class)
                .initializer("source")
                .addModifiers(KModifier.PRIVATE)
                .build()
        )
        for (configDef in configs) {
            when (configDef) {
                is IntConfigDef -> {
                    val constraints = configDef.constraints.map {
                        when (it) {
                            "nonnegative" -> NonNegativeConstraint::class
                            else -> throw IllegalArgumentException("Unsupported constraint: $it")
                        }
                    }
                    val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
                    configClass.addProperty(
                        PropertySpec.builder(configDef.key, Int::class)
                            .delegate(
                                "%T(%S, %N, %L, listOf($constraintsInterpolation))",
                                IntConfigurationValue::class,
                                configDef.key,
                                "source",
                                configDef.defaultValue,
                                *constraints.toTypedArray()
                            )
                            .build()
                    )
                }
                is StringConfigDef -> {
                    val constraints = emptyList<Nothing>() // TODO populate
                    val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
                    configClass.addProperty(
                        PropertySpec.builder(configDef.key, String::class)
                            .delegate(
                                "%T(%S, %N, %S, listOf($constraintsInterpolation))",
                                StringConfigurationValue::class,
                                configDef.key,
                                "source",
                                configDef.defaultValue.orEmpty(),
                                *constraints.toTypedArray()
                            )
                            .build()
                    )
                }
            }
        }
        val packageName = node.get("package").textValue()
        return FileSpec.builder(packageName, className)
            .addType(configClass.build())
            .build()
    }
}
