import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
    java
    `maven-publish`
    signing
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("org.jetbrains.dokka")
}

java {
    withSourcesJar()
}

// Create a javadoc JAR from Dokka's HTML output
val dokkaHtmlJar by tasks.registering(Jar::class) {
    dependsOn("dokkaGenerateModuleHtml")
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("dokka/html"))
}

// Ensure metadata generation tasks depend on the javadoc JAR
tasks.withType<GenerateModuleMetadata>().configureEach {
    dependsOn(dokkaHtmlJar)
}

if (IsCI) {
    // Disable all signing tasks on CI (no signatory available)
    tasks.withType<Sign>().configureEach {
        enabled = false
    }
}

afterEvaluate {
    addSonatypeRepository()

    publishing.publications.asSequence()
        .filterIsInstance<MavenPublication>()
        .onEach { publication ->
            // Only add javadoc JAR to the "maven" publication (ours), not plugin-publish's publications
            if (publication.name == "maven") {
                publication.artifact(dokkaHtmlJar)
            }
            if (!IsCI) {
                signing {
                    sign(publication)
                }
            }
        }
        .forEach(MavenPublication::attachCommonPomMetadata)
}
