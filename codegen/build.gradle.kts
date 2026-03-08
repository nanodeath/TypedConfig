plugins {
    kotlin("jvm")
    id("com.gradleup.shadow") version "9.0.0"
    application
    id("typedconfig.published-conventions")
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:${libs.versions.jackson.get()}"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml")
    implementation("com.github.ajalt.clikt:clikt:5.1.0")
    implementation("org.apache.commons:commons-text:1.15.0")
    api("com.squareup:kotlinpoet:2.2.0")
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("dev.madetobuild.typedconfig.codegen.TypedConfigCliKt")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("TypedConfig Codegen")
                description.set("Standalone Java application that translates a TypedConfig spec into Kotlin")
            }
        }
    }
}
