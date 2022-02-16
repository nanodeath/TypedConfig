import name.maxaller.konfigurator.generate.ConfigurationReader
import java.io.File

fun main(args: Array<String>) {
    val inputDirectory = File(requireNotNull(args.getOrNull(0)))
    val outputDirectory = File(requireNotNull(args.getOrNull(1)))
    val konfigs = inputDirectory.walkTopDown()
        .filter { it.endsWith("konfig.toml") }
        .filterNot { "invalid" in it.toString() }
        .toList()
    for (konfig in konfigs) {
        ConfigurationReader().readFile(konfig)
            .writeTo(outputDirectory)

    }
    println("konfigs: $konfigs")
}
