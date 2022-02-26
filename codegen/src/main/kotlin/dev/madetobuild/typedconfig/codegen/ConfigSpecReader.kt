package dev.madetobuild.typedconfig.codegen

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import dev.madetobuild.typedconfig.codegen.keys.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.time.Instant
import java.time.temporal.ChronoUnit

private val sourceClassName = ClassName("$RUNTIME_PACKAGE.source", "Source")
private val keyClassName = ClassName(RUNTIME_PACKAGE, "Key")
private val generatedConfigClassName = ClassName(RUNTIME_PACKAGE, "GeneratedConfig")
private val typedConfigClassName = ClassName(RUNTIME_PACKAGE, "TypedConfig")

@Suppress("TooManyFunctions")
class ConfigSpecReader {
    private val tomlMapper = TomlMapper()

    // FIXME associateBy is problematic -- it ignores duplicate keys. We should throw.
    // TODO this obviously shouldn't be a hardcoded list, should be scanned.
    private val keyTypeGenerators =
        listOf(
            IntegerKey.Generator,
            StringKey.Generator,
            DoubleKey.Generator,
            BooleanKey.Generator,
            DurationKey.Generator
        ).associateBy { it.type }

    private val collectionKeyTypeGenerators =
        listOf(
            ListKey.Generator,
            SetKey.Generator
        ).associateBy { it.type }

    fun translateIntoCode(file: File): FileSpec = translateIntoCode(tomlMapper.readTree(file), file.path)
    fun translateIntoCode(inputStream: InputStream, name: String = "<inputStream>"): FileSpec =
        translateIntoCode(tomlMapper.readTree(inputStream), name)

    fun translateIntoCode(reader: Reader, name: String = "<reader>"): FileSpec =
        translateIntoCode(tomlMapper.readTree(reader), name)

    private fun translateIntoCode(jsonNode: JsonNode, inputName: String): FileSpec {
        val node: JsonNode = jsonNode
        val className = node.get("class").textValue().let { classString ->
            ClassName.bestGuess(classString)
        }
        val description: String? = node.path("description").textValue()
        val namespace = node.path("namespace").textValue().takeUnless { it.isNullOrBlank() }
        val keys: List<Key<*>> =
            parseKeyTypes(node, precedingKey = namespace?.let { listOf(it) } ?: emptyList())

        val configClass = initializeMainConfigClass(className)

        addDescription(configClass, description)
        addGeneratedAnnotation(configClass, inputName)
        addSupertypes(configClass)

        val innerClasses = mutableMapOf<InnerTypeSpec, TypeSpec.Builder>()

        val configProperties: Map<TypeSpec.Builder, List<PropertySpec>> =
            associateEnclosingClassToProperties(configClass, className, keys, innerClasses)

        configProperties.forEach { (typeSpec, propertySpecs) -> typeSpec.addProperties(propertySpecs) }

        generateValidateMethod(configClass, configProperties, innerClasses)
        generateGetAllKeysMethod(configProperties, innerClasses)

        processInnerClasses(innerClasses)

        addCompanionObject(configClass, className)

        return FileSpec.builder(className.packageName, className.simpleName)
            .addType(configClass.build())
            .build()
    }

    private fun generateValidateMethod(
        configClass: TypeSpec.Builder,
        configProperties: Map<TypeSpec.Builder, List<PropertySpec>>,
        innerClasses: Map<InnerTypeSpec, TypeSpec.Builder>
    ) {
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
    }

    private fun generateGetAllKeysMethod(
        configProperties: Map<TypeSpec.Builder, List<PropertySpec>>,
        innerClasses: Map<InnerTypeSpec, TypeSpec.Builder>
    ) {
        for ((typeSpec, propertySpecs) in configProperties) {
            val myInnerTypes = innerClasses.keys.filter { it.enclosingClass === typeSpec }
            typeSpec.addFunction(FunSpec.builder("getAllKeys").apply {
                returns(
                    ClassName("kotlin.sequences", "Sequence")
                        .parameterizedBy(
                            ClassName("kotlin.reflect", "KProperty0").parameterizedBy(starType)
                        )
                )
                addModifiers(KModifier.OVERRIDE)
                addKdoc(
                    """
                        |Returns a sequence of all key properties.
                    """.trimMargin()
                )
                addCode(CodeBlock.builder()
                    .add("return sequenceOf<KProperty0<*>>(")
                    .also { block ->
                        propertySpecs.forEachIndexed { index, prop ->
                            val suffix = if (index == propertySpecs.lastIndex) "" else ", "
                            block.add("::%N$suffix", prop.name)
                        }
                    }
                    .add(")")
                    .also { block ->
                        myInnerTypes.forEach { block.add(" + %N.getAllKeys()", it.fieldName) }
                    }
                    .build())
            }
                .build())
        }
    }

    private fun associateEnclosingClassToProperties(
        mainConfigClass: TypeSpec.Builder,
        mainClassName: ClassName,
        keys: List<Key<*>>,
        innerClasses: MutableMap<InnerTypeSpec, TypeSpec.Builder>
    ): Map<TypeSpec.Builder, List<PropertySpec>> {
        val propertiesByClass: Map<TypeSpec.Builder, List<PropertySpec>> = keys.groupBy(
            keySelector = { key ->
                getOrCreateClassToUpdate(key.key, innerClasses, mainConfigClass, mainClassName)
            },
            valueTransform = { key ->
                PropertySpec.builder(
                    key.key.substringAfterLast('.'),
                    key.type.copy(nullable = !key.metadata.required)
                )
                    .addAnnotation(
                        AnnotationSpec
                            .builder(keyClassName)
                            .addMember("name = %S", key.key)
                            .useSiteTarget(AnnotationSpec.UseSiteTarget.PROPERTY)
                            .build()
                    )
                    .delegate(
                        key.templateString,
                        *key.templateArgs
                    )
                    .addKdoc(key.kdoc)
                    .build()
            })
        // In certain cases (e.g. namespaces), the main config class won't have any direct properties, just inner
        // classes. We add this stub in just in case to make consuming the returned value easier.
        // Note that this stub will end up getting overwritten if we actually do have properties (that is,
        // if propertiesByClass[mainConfigClass] is set).
        val stubForMainClass = mapOf(mainConfigClass to emptyList<PropertySpec>())
        // This can affect empty inner classes, too, e.g. outer.inner.key, where outer has no other keys.
        val stubForInnerClasses = innerClasses.values.map { it to emptyList<PropertySpec>() }
        return stubForMainClass + stubForInnerClasses + propertiesByClass
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

    private fun addGeneratedAnnotation(configClass: TypeSpec.Builder, name: String) {
        configClass.addAnnotation(
            AnnotationSpec.builder(ClassName("javax.annotation", "Generated"))
                .addMember("%S", ConfigSpecReader::class.qualifiedName!!)
                .addMember("date = %S", Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .addMember("comments = %S", "Generated from $name")
                .build()
        )
    }

    private fun addSupertypes(configClass: TypeSpec.Builder) {
        configClass.addSuperinterface(generatedConfigClassName)
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
                            "return %T(%T.%N)", className, typedConfigClassName, "defaultSource"
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
                    .also(::addSupertypes)
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

    private fun parseKeyTypes(
        node: JsonNode,
        precedingKey: List<String> = emptyList()
    ): List<Key<*>> = node.fields().asSequence()
        .filter { (_, v) -> v.isObject }
        .flatMap { (key, value) ->
            require(key.isNotBlank()) { "Key cannot be empty or blank, was `${key}`" }

            val fullKey = if (precedingKey.isEmpty()) key else "${precedingKey.joinToString(".")}.$key"

            val type = value.path("type").textValue()

            if (type != null) {
                if (type in keyTypeGenerators) {
                    val keyTypeGenerator = keyTypeGenerators[type]!!

                    val checks: List<ClassName>? = (value.get("checks") as ArrayNode?)
                        ?.map(JsonNode::textValue)
                        ?.map(keyTypeGenerator::mapChecks)
                    val metadata = KeyMetadata(
                        description = value.get("description")?.textValue(),
                        required = value.get("required")?.booleanValue() ?: true,
                        sensitive = value.get("sensitive")?.booleanValue() ?: false
                    )
                    sequenceOf(
                        keyTypeGenerator.generate(
                            fullKey, value.get("default")?.asText(), checks.orEmpty(), metadata
                        ).validate(type)
                    )
                } else {
                    val collectionPattern = Regex("(\\w+)<(\\w*)>")
                    val match = collectionPattern.matchEntire(type)
                    if (match != null) {
                        val (collectionType, valueType) = match.destructured
                        val collectionReader = collectionKeyTypeGenerators[collectionType]
                        val valueTypeReader = keyTypeGenerators[valueType]
                        if (collectionReader != null && valueTypeReader != null) {
                            val metadata = KeyMetadata(
                                description = value.get("description")?.textValue(),
                                required = value.get("required")?.booleanValue() ?: true,
                                sensitive = value.get("sensitive")?.booleanValue() ?: false
                            )
                            val default = value.get("default")?.elements()?.asSequence()?.map { it.asText() }?.toList()
                            val genericKeyType = valueTypeReader
                                .generate("", null, emptyList(), KeyMetadata(null, required = true, sensitive = false))
                            return@flatMap sequenceOf(
                                collectionReader.generate(
                                    fullKey,
                                    default,
                                    metadata,
                                    genericKeyType
                                )
                            )
                        }
                    }
                    // TODO more specific exception
                    throw IllegalArgumentException("Unsupported type: `${type}`")
                }
            } else if (value.isObject) {
                parseKeyTypes(value, precedingKey + listOf(key)).asSequence()
            } else {
                // FIXME this is an error
                emptySequence()
            }
        }
        .toList()
}

private fun <T> Key<T>.validate(type: String): Key<T> = apply {
    if (!supportsSensitiveFlag && metadata.sensitive) {
        throw UnsupportedOperationException(
            "The `$type` type (for key '${this.key}') doesn't support the `sensitive` attribute")
    }
}
