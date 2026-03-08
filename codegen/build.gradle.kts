plugins {
    kotlin("jvm")
    alias(libs.plugins.shadow)
    application
    id("typedconfig.published-conventions")
}

dependencies {
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.toml)
    implementation(libs.clikt)
    implementation(libs.commons.text)
    api(libs.kotlinpoet)
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
