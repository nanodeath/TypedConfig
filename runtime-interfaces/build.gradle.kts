plugins {
    kotlin("jvm")
    id("typedconfig.published-conventions")
}

dependencies {
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("TypedConfig Runtime Interfaces")
                description.set("Simple interfaces used by TypedConfig plugins")
            }
        }
    }
}
