rootProject.name = "TypedConfig"

// Production projects
include("codegen", "runtime", "runtime-interfaces")
// Other sources
include("runtime-source-json")
// Plugins
include("typedconfig-gradle-plugin")
// Modules for third-party integration
include("runtime-module-koin")

// Test projects
include("codegen-test-config", "tests")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Production dependencies
            version("jackson", "2.18.0")
            version("junit5", "5.11.1")
            version("sl4fj", "2.0.16")
            library("slf4j", "org.slf4j", "slf4j-api").versionRef("sl4fj")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("sl4fj")

            // Test dependencies
            library("junit5", "org.junit.jupiter", "junit-jupiter").versionRef("junit5")
            library("junit5-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit5")
            library("kotest-assertions", "io.kotest:kotest-assertions-core:5.9.1")
            library("mockk", "io.mockk:mockk:1.13.12")
            bundle("tests", listOf("junit5", "kotest-assertions", "mockk", "slf4j-simple"))
        }
    }
}
include("runtime-test-common")
