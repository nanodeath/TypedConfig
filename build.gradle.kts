plugins {
    kotlin("jvm") version "2.3.10"
    id("org.jetbrains.kotlinx.kover") version "0.9.7"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    signing
}

group = "dev.madetobuild.typedconfig"
version = "0.3-SNAPSHOT"

allprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    kotlin {
        jvmToolchain(21)
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        buildUponDefaultConfig = true
    }

    afterEvaluate {
        // Run Detekt towards the start of the build instead of the end, for time efficiency...
        tasks.test {
            shouldRunAfter("detekt")
        }
    }
}
