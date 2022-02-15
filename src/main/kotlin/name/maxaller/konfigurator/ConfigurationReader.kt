import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.squareup.kotlinpoet.*
import name.maxaller.konfigurator.IntConfig
import name.maxaller.konfigurator.runtime.IntConfigurationValue
import name.maxaller.konfigurator.runtime.Source
import name.maxaller.konfigurator.runtime.constraints.NonNegativeConstraint
import java.io.File

class ConfigurationReader {
    private val tomlMapper = TomlMapper()

    fun readFile(file: File): FileSpec {
        val node = tomlMapper.readTree(file)
        val configs = mutableListOf<IntConfig>()
        for ((key, value) in node.fields().asSequence().filter { (_, v) -> v.isObject }) {
            require(key.isNotBlank()) { "Key cannot be empty or blank, was `${key}` in $file" }
            when (val datatype = value.path("datatype").textValue()) {
                "int" -> {
                    val defaultValue: Int? = value.get("default")?.intValue()
                    val constraints: List<String>? = (value.get("constraints") as ArrayNode?)?.map { it.textValue() }

                    configs.add(IntConfig(key, defaultValue, constraints))
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
        for (config in configs) {
            val constraints = config.constraints.orEmpty().map {
                when (it) {
                    "nonnegative" -> NonNegativeConstraint::class
                    else -> throw IllegalArgumentException("Unsupported constraint: $it")
                }
            }
            val constraintsInterpolation = constraints.joinToString(", ") { "%T" }
            configClass.addProperty(
                PropertySpec.builder(config.key, Int::class)
                    .delegate(
                        "%T(%N, %L, listOf($constraintsInterpolation))",
                        IntConfigurationValue::class,
                        "source",
                        config.defaultValue,
                        *constraints.toTypedArray()
                    )
                    .build()
            )
        }
        val packageName = node.get("package").textValue()
        return FileSpec.builder(packageName, className)
            .addType(configClass.build())
            .build()
    }
}

//fun main() {
//    ConfigurationReader().readFile(Paths.get("/home/maxaller/Projects/konfigurator/tool/src/test/resources/basic_test/konfig.toml"))
//
//    GeneratedConfig(EnvSource).maxLoginTries.let { println(it) }
//}
