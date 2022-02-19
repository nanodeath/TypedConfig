package com.github.nanodeath.typedconfig.generate

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.github.nanodeath.typedconfig.generate.configdef.*
import com.github.nanodeath.typedconfig.generate.configdef.DoubleConfigDefGenerator
import com.github.nanodeath.typedconfig.generate.configdef.IntConfigDefGenerator
import com.github.nanodeath.typedconfig.generate.configdef.StringConfigDefGenerator
import com.squareup.kotlinpoet.*
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

private val sourceClassName = ClassName("$basePkg.source", "Source")

class ConfigurationReader {
    private val tomlMapper = TomlMapper()

    // FIXME associateBy is problematic -- it ignores duplicate keys. We should throw.
    // TODO this obviously shouldn't be a hardcoded list, should be scanned.
    private val configDefReaders =
        listOf(IntConfigDefGenerator, StringConfigDefGenerator, DoubleConfigDefGenerator).associateBy { it.key }

    fun readFile(file: File): FileSpec {
        val node = tomlMapper.readTree(file)
        val packageName = node.get("package").textValue()
        val className = ClassName(packageName, node.get("class").textValue())
        val description = node.path("description").textValue()
        val configSpecs: List<ConfigSpec<*>> = parseConfigDefs(node, file)

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
        configClass.addType(
            TypeSpec.companionObjectBuilder("Factory")
                .addFunction(
                    FunSpec.builder("default").returns(className)
                        .addCode("return %T(%T.%N)", className, ClassName(basePkg, "TypedConfig"), "defaultSource")
                        .build()
                )
                .build()
        )

        if (!description.isNullOrBlank()) {
            configClass.addKdoc(description)
        }

        addGeneratedAnnotation(configClass, file)

        val innerClasses = mutableMapOf<InnerTypeSpec, TypeSpec.Builder>()
        for (configSpec in configSpecs) {
            val (configDef, metadata) = configSpec
            val classToUpdate = getClassToUpdate(configDef.key, innerClasses, configClass, className)

            val constraints = configDef.constraints
            val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
            classToUpdate.addProperty(
                PropertySpec.builder(configDef.key.substringAfterLast('.'), configDef.type)
                    .delegate(
                        "%T(%S, %N, %L, listOf($constraintsInterpolation))",
                        configDef.keyClass,
                        configDef.key,
                        "source",
                        configDef.defaultValue,
                        *constraints.toTypedArray()
                    )
                    .also { if (!metadata.description.isNullOrBlank()) it.addKdoc(metadata.description) }
                    .build()
            )
        }
        for ((innerTypeSpec, innerTypeBuilder) in innerClasses.toList().asReversed()) {
            val enclosingClassName = innerTypeSpec.enclosingClassName
            innerTypeSpec.enclosingClass.addProperty(
                PropertySpec.builder(innerTypeSpec.fieldName, enclosingClassName.nestedClass(innerTypeSpec.className))
                    .initializer("%T()", enclosingClassName.nestedClass(innerTypeSpec.className))
                    .build()
            )
            innerTypeSpec.enclosingClass.addType(innerTypeBuilder.build())
        }
        return FileSpec.builder(packageName, className.simpleName)
            .addType(configClass.build())
            .build()
    }

    private fun addGeneratedAnnotation(configClass: TypeSpec.Builder, file: File) {
        configClass.addAnnotation(
            AnnotationSpec.builder(ClassName("javax.annotation", "Generated"))
                .addMember("%S", ConfigurationReader::class.qualifiedName!!)
                .addMember("date = %S", Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .addMember("comments = %S", "Generated from $file")
                .build()
        )
    }

    private fun getClassToUpdate(
        key: String,
        innerClasses: MutableMap<InnerTypeSpec, TypeSpec.Builder>,
        enclosingClass: TypeSpec.Builder,
        enclosingClassName: ClassName
    ): TypeSpec.Builder = if ('.' in key) {
        val field = key.substringBefore('.')

        val innerName = field.replaceFirstChar { it.uppercase() }
        val newEnclosingClass =
            innerClasses.getOrPut(InnerTypeSpec(field, innerName, enclosingClass, enclosingClassName)) {
                TypeSpec.classBuilder(innerName)
                    .addModifiers(KModifier.INNER)
                    .primaryConstructor(
                        FunSpec.constructorBuilder().addModifiers(KModifier.INTERNAL).build()
                    )
            }
        getClassToUpdate(
            key.substringAfter('.'),
            innerClasses,
            newEnclosingClass,
            enclosingClassName.nestedClass(innerName)
        )
    } else enclosingClass

    private data class InnerTypeSpec(
        val fieldName: String,
        val className: String,
        val enclosingClass: TypeSpec.Builder,
        val enclosingClassName: ClassName
    )

    private fun parseConfigDefs(
        node: JsonNode,
        file: File,
        precedingKey: List<String> = emptyList()
    ): List<ConfigSpec<*>> = node.fields().asSequence()
        .filter { (_, v) -> v.isObject }
        .flatMap { (key, value) ->
            require(key.isNotBlank()) { "Key cannot be empty or blank, was `${key}` in $file" }

            val fullKey = if (precedingKey.isEmpty()) key else "${precedingKey.joinToString(".")}.$key"

            val type = value.path("type").textValue()

            if (type != null) {
                // TODO more specific exception
                val configDefReader =
                    configDefReaders[type] ?: throw IllegalArgumentException("Unsupported type: `${type}` in $file")

                val constraints: List<ClassName>? = (value.get("constraints") as ArrayNode?)
                    ?.map(JsonNode::textValue)
                    ?.map(configDefReader::mapConstraint)
                sequenceOf(
                    ConfigSpec(
                        configDefReader.generate(fullKey, value.get("default")?.asText(), constraints.orEmpty()),
                        ConfigDefMetadata(value.get("description")?.textValue())
                    )
                )
            } else if (value.isObject) {
                parseConfigDefs(value, file, precedingKey + listOf(key)).asSequence()
            } else {
                // FIXME this is an error
                emptySequence()
            }
        }
        .toList()
}
