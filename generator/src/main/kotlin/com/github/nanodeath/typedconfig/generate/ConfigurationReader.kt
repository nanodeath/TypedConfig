package com.github.nanodeath.typedconfig.generate

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.github.nanodeath.typedconfig.generate.configdef.*
import com.squareup.kotlinpoet.*
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

private val sourceClassName = ClassName("$RUNTIME_PACKAGE.source", "Source")

class ConfigurationReader {
    private val tomlMapper = TomlMapper()

    // FIXME associateBy is problematic -- it ignores duplicate keys. We should throw.
    // TODO this obviously shouldn't be a hardcoded list, should be scanned.
    private val configDefReaders =
        listOf(
            IntConfigDefGenerator,
            StringConfigDefGenerator,
            DoubleConfigDefGenerator,
            BooleanConfigDefGenerator
        ).associateBy { it.key }

    fun readFile(file: File): FileSpec {
        val node = tomlMapper.readTree(file)
        val packageName = node.get("package").textValue()
        val className = ClassName(packageName, node.get("class").textValue())
        val description: String? = node.path("description").textValue()
        val namespace = node.path("namespace").textValue().takeUnless { it.isNullOrBlank() }
        val configDefs: List<ConfigDef<*>> =
            parseConfigDefs(node, file, precedingKey = namespace?.let { listOf(it) } ?: emptyList())

        val configClass = initializeMainConfigClass(className)

        addDescription(configClass, description)
        addGeneratedAnnotation(configClass, file)

        val innerClasses = mutableMapOf<InnerTypeSpec, TypeSpec.Builder>()

        val configProperties: Map<TypeSpec.Builder, List<PropertySpec>> =
            associateEnclosingClassToProperties(configClass, className, configDefs, innerClasses)

        configProperties.forEach { (typeSpec, propertySpecs) -> typeSpec.addProperties(propertySpecs) }

        for ((typeSpec, propertySpecs) in configProperties) {
            val myInnerTypes = innerClasses.keys.filter { it.enclosingClass === typeSpec }
            typeSpec.addFunction(FunSpec.builder("validate").apply {
                addKdoc(
                    """
                    |Checks that all required keys have been provided values.
                    |@throws %T
                """.trimMargin(),
                    missingConfigurationExceptionName
                )
                beginControlFlow("return apply {")
                propertySpecs.forEach { addStatement("%N", it.name) }
                myInnerTypes.forEach { addStatement("%N.validate()", it.fieldName) }
                if (typeSpec !== configClass) {
                    addModifiers(KModifier.INTERNAL)
                }
                endControlFlow()
            }
                .build())
        }

        processInnerClasses(innerClasses)

        addCompanionObject(configClass, className)

        return FileSpec.builder(packageName, className.simpleName)
            .addType(configClass.build())
            .build()
    }

    private fun associateEnclosingClassToProperties(
        mainConfigClass: TypeSpec.Builder,
        mainClassName: ClassName,
        configDefs: List<ConfigDef<*>>,
        innerClasses: MutableMap<InnerTypeSpec, TypeSpec.Builder>
    ): Map<TypeSpec.Builder, List<PropertySpec>> {
        val propertiesByClass: Map<TypeSpec.Builder, List<PropertySpec>> = configDefs.groupBy(
            keySelector = { configDef ->
                getOrCreateClassToUpdate(configDef.key, innerClasses, mainConfigClass, mainClassName)
            },
            valueTransform = { configDef ->
                val configDefProperty = ConfigDefProperty(configDef)
                PropertySpec.builder(configDef.key.substringAfterLast('.'), configDefProperty.type)
                    .delegate(
                        "%T(%S, %N, %L, listOf(${configDefProperty.constraintsInterpolation}))",
                        configDef.keyClass,
                        configDef.key,
                        "source",
                        configDef.defaultValue,
                        *configDefProperty.constraints.toTypedArray()
                    )
                    .addKdoc(configDefProperty.kdoc)
                    .build()
            })
        // In certain cases (e.g. namespaces), the main config class won't have any direct properties, just inner
        // classes. We add this stub in just in case to make consuming the returned value easier.
        // Note that this stub will end up getting overwritten if we actually do have properties (that is,
        // if propertiesByClass[mainConfigClass] is set).
        val stubForMainClass = mapOf(mainConfigClass to emptyList<PropertySpec>())
        return stubForMainClass + propertiesByClass
    }

    private fun initializeMainConfigClass(className: ClassName): TypeSpec.Builder =
        TypeSpec.classBuilder(className).apply {
            primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("source", sourceClassName)
                    .build()
            )
            addProperty(
                PropertySpec.builder("source", sourceClassName)
                    .initializer("source")
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        }

    private fun addDescription(configClass: TypeSpec.Builder, description: String?) {
        if (!description.isNullOrBlank()) {
            configClass.addKdoc(description)
        }
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

    /**
     * Ensure inner classes get initialized and attached properly to their enclosing classes.
     */
    private fun processInnerClasses(innerClasses: MutableMap<InnerTypeSpec, TypeSpec.Builder>) {
        for ((innerTypeSpec, innerTypeBuilder) in innerClasses.toList().asReversed()) {
            val enclosingClassName = innerTypeSpec.enclosingClassName
            innerTypeSpec.enclosingClass.addProperty(
                PropertySpec.builder(innerTypeSpec.fieldName, enclosingClassName.nestedClass(innerTypeSpec.className))
                    .initializer("%T()", enclosingClassName.nestedClass(innerTypeSpec.className))
                    .build()
            )
            innerTypeSpec.enclosingClass.addType(innerTypeBuilder.build())
        }
    }

    private fun addCompanionObject(
        configClass: TypeSpec.Builder, className: ClassName
    ) {
        configClass.addType(
            TypeSpec.companionObjectBuilder("Factory")
                .addFunction(
                    FunSpec.builder("default")
                        .returns(className)
                        .addCode(
                            "return %T(%T.%N)", className, ClassName(RUNTIME_PACKAGE, "TypedConfig"), "defaultSource"
                        )
                        .build()
                )
                .build()
        )
    }

    private fun getOrCreateClassToUpdate(
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
        getOrCreateClassToUpdate(
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
    ): List<ConfigDef<*>> = node.fields().asSequence()
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
                val metadata = ConfigDefMetadata(
                    description = value.get("description")?.textValue(),
                    required = value.get("required")?.booleanValue() ?: true
                )
                sequenceOf(
                    configDefReader.generate(
                        fullKey, value.get("default")?.asText(), constraints.orEmpty(), metadata
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
