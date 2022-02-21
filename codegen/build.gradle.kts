plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
    `maven-publish`
    signing
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:${libs.versions.jackson.get()}"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml")
    api("com.squareup:kotlinpoet:1.10.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("com.github.nanodeath.typedconfig.generate.GeneratorKt")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

addSonatypeRepository()

signing {
    sign(publishing.publications["maven"])
}
