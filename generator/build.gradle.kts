plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

dependencies {
    implementation(enforcedPlatform("com.fasterxml.jackson:jackson-bom:${libs.versions.jackson.get()}"))
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
