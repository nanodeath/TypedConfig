import com.github.nanodeath.typedconfig.codegen.ConfigurationReader
import java.io.File

fun main(args: Array<String>) {
    val inputDirectory = File(requireNotNull(args.getOrNull(0)))
    val outputDirectory = File(requireNotNull(args.getOrNull(1)))
    val configs = inputDirectory.walkTopDown()
        .filter { it.endsWith("config.tc.toml") }
        .filterNot { "invalid" in it.toString() }
        .toList()
    for (config in configs) {
        ConfigurationReader().readFile(config)
            .writeTo(outputDirectory)

    }
    println("configs: $configs")
}
