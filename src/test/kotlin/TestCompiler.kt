import name.maxaller.konfigurator.generate.ConfigurationReader
import java.io.File

fun main() {
    val konfigs = File("src/test/resources/").walkTopDown()
        .filter { it.endsWith("konfig.toml") }
        .filterNot { "invalid" in it.toString() }
        .toList()
    for (konfig in konfigs) {
        ConfigurationReader().readFile(konfig)
            .writeTo(File("build/generated-sources-test"))

    }
    println("konfigs: $konfigs")
}
