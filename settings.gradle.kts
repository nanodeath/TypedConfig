rootProject.name = "TypedConfig"

// Production projects
include("generator", "runtime", "runtime-interfaces")
// Other sources
include("runtime-source-json")
// Plugins
include("gradle-plugin")

// Test projects
include("generator-test-config", "tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Production dependencies
            version("jackson", "2.13.1")
            version("junit5", "5.8.2")

            // Test dependencies
            library("junit5", "org.junit.jupiter", "junit-jupiter").versionRef("junit5")
            library("junit5-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit5")
            library("kotest-assertions", "io.kotest:kotest-assertions-core:5.1.0")
            library("mockk", "io.mockk:mockk:1.12.2")
            bundle("tests", listOf("junit5", "kotest-assertions", "mockk"))
        }
    }
}
