plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
}

group = "com.github.nanodeath.typedconfig"
version = "1.0-SNAPSHOT"

allprojects {
    version = rootProject.version
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
}
