plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    signing
}

group = "com.github.nanodeath"
version = "0.1"

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

        // Attach Apache-2.0 license to everything that publishes.
        plugins.withType<MavenPublishPlugin> {
            val ext = extensions.getByType<PublishingExtension>()
            ext.publications.filterIsInstance<MavenPublication>().forEach { publication ->
                publication.artifactId = "typedconfig-$name"
                publication.attachApacheLicense()
            }
        }
    }
}
