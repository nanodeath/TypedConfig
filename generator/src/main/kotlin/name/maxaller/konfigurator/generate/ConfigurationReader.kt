package name.maxaller.konfigurator.generate

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.squareup.kotlinpoet.*
import java.io.File

class ConfigurationReader {
    private val tomlMapper = TomlMapper()

    fun readFile(file: File): FileSpec {
        val node = tomlMapper.readTree(file)
        val configs: List<ConfigDef> = parseNode(node, file)

        val packageName = node.get("package").textValue()
        val className = ClassName(packageName, node.get("class").textValue())
        val configClass = TypeSpec.classBuilder(className)
        configClass.primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("source", sourceClassName)
                .build()
        )
        configClass.addProperty(
            PropertySpec.builder("source", sourceClassName)
                .initializer("source")
                .addModifiers(KModifier.PRIVATE)
                .build()
        )
        val innerClasses = mutableMapOf<InnerTypeSpec, TypeSpec.Builder>()
        for (configDef in configs) {
            when (configDef) {
                is IntConfigDef -> {
                    val classToUpdate = getClassToUpdate(configDef.key, innerClasses, configClass)

                    val constraints = configDef.constraints.map {
                        when (it) {
                            "nonnegative" -> nonNegativeIntClassName
                            else -> throw IllegalArgumentException("Unsupported constraint: $it")
                        }
                    }
                    val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
                    classToUpdate.addProperty(
                        PropertySpec.builder(configDef.key.substringAfterLast('.'), Int::class)
                            .delegate(
                                "%T(%S, %N, %L, listOf($constraintsInterpolation))",
                                intKeyClassName,
                                configDef.key,
                                "source",
                                configDef.defaultValue,
                                *constraints.toTypedArray()
                            )
                            .build()
                    )
                }
                is StringConfigDef -> {
                    val classToUpdate = getClassToUpdate(configDef.key, innerClasses, configClass)

                    val constraints = configDef.constraints.map {
                        when (it) {
                            "notblank" -> notBlankStringClassName
                            else -> throw IllegalArgumentException("Unsupported constraint: $it")
                        }
                    }
                    val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
                    classToUpdate.addProperty(
                        PropertySpec.builder(configDef.key.substringAfterLast('.'), String::class)
                            .delegate(
                                "%T(%S, %N, %S, listOf($constraintsInterpolation))",
                                stringKeyClassName,
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
        for ((innerTypeSpec, innerTypeBuilder) in innerClasses) {
            configClass.addProperty(
                PropertySpec.builder(innerTypeSpec.fieldName, className.nestedClass(innerTypeSpec.className))
                    .initializer("%T()", className.nestedClass(innerTypeSpec.className))
                    .build()
            )
            configClass.addType(innerTypeBuilder.build())
        }
        return FileSpec.builder(packageName, className.simpleName)
            .addType(configClass.build())
            .build()
    }

    private fun getClassToUpdate(
        key: String,
        innerClasses: MutableMap<InnerTypeSpec, TypeSpec.Builder>,
        configClass: TypeSpec.Builder
    ) = if ('.' in key) {
        // FIXME only supports one level of nesting for now
        val field = key.substringBeforeLast('.').split('.').single()
        val innerName = field.replaceFirstChar { it.uppercase() }
        innerClasses.getOrPut(InnerTypeSpec(field, innerName)) {
            TypeSpec.classBuilder(innerName)
                .addModifiers(KModifier.INNER)
                .primaryConstructor(
                    FunSpec.constructorBuilder().addModifiers(KModifier.INTERNAL).build()
                )
        }
    } else {
        configClass
    }

    private data class InnerTypeSpec(val fieldName: String, val className: String)

    private fun parseNode(
        node: JsonNode,
        file: File,
        precedingKey: List<String> = emptyList()
    ): List<ConfigDef> = node.fields().asSequence()
        .filter { (_, v) -> v.isObject }
        .flatMap { (key, value) ->
            require(key.isNotBlank()) { "Key cannot be empty or blank, was `${key}` in $file" }

            val fullKey = if (precedingKey.isEmpty()) key else "${precedingKey.joinToString(".")}.$key"

            when (val datatype = value.path("datatype").textValue()) {
                "int" -> {
                    val defaultValue: Int? = value.get("default")?.intValue()
                    val constraints: List<String>? =
                        (value.get("constraints") as ArrayNode?)?.map { it.textValue() }

                    sequenceOf(IntConfigDef(fullKey, defaultValue, constraints.orEmpty()))
                }
                "str" -> {
                    val defaultValue: String? = value.get("default")?.textValue()
                    val constraints: List<String>? =
                        (value.get("constraints") as ArrayNode?)?.map { it.textValue() }

                    sequenceOf(StringConfigDef(fullKey, defaultValue, constraints.orEmpty()))
                }
                null -> {
                    if (value.isObject) {
                        parseNode(value, file, precedingKey + listOf(key)).asSequence()
                    } else {
                        emptySequence()
                    }
                }
                else -> throw IllegalArgumentException("Unsupported datatype: `${datatype}` in $file")
            }
        }
        .toList()
}
