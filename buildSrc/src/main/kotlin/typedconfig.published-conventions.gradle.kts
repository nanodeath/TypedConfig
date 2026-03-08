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
    dependsOn("dokkaHtml")
    archiveClassifier.set("javadoc")
    from(File(buildDir, "dokka/html"))
}

afterEvaluate {
    addSonatypeRepository()

    publishing.publications.asSequence()
        .filterIsInstance<MavenPublication>()
        .onEach { publication ->
            publication.artifact(dokkaHtmlJar.get()) {
                classifier = "javadoc"
            }
            if (!IsCI) {
                signing {
                    sign(publication)
                }
            }
        }
        .forEach(MavenPublication::attachCommonPomMetadata)
}
