plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlinx.kover") version "0.8.3"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
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
