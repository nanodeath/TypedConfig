import com.github.nanodeath.typedconfig.codegen.ConfigSpecReader
import java.io.File

fun main(args: Array<String>) {
    val inputDirectory = File(requireNotNull(args.getOrNull(0)))
    val outputDirectory = File(requireNotNull(args.getOrNull(1)))
    val configs = inputDirectory.walkTopDown()
        .filter { it.endsWith("config.tc.toml") }
        .filterNot { "invalid" in it.toString() }
        .toList()
    for (config in configs) {
        ConfigSpecReader().translateIntoCode(config)
            .writeTo(outputDirectory)

    }
    println("configs: $configs")
}
