import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing

plugins {
    java
    `maven-publish`
    signing
}

java {
    withSourcesJar()
    withJavadocJar()
}

afterEvaluate {
    addSonatypeRepository()

    publishing.publications.asSequence()
        .filterIsInstance<MavenPublication>()
        .onEach { publication ->
            if (!IsCI) {
                signing {
                    sign(publication)
                }
            }
        }
        .forEach(MavenPublication::attachCommonPomMetadata)
}
