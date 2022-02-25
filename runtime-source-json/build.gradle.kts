plugins {
    kotlin("jvm")
    id("typedconfig.published-conventions")
}

dependencies {
    implementation(project(":runtime-interfaces"))
    implementation("com.fasterxml.jackson.jr:jackson-jr-objects:${libs.versions.jackson.get()}")
    testImplementation(libs.bundles.tests)
    testImplementation(project(":runtime-test-common"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("TypedConfig Runtime Source JSON")
                description.set("A Source for TypedConfig that parses JSON files")
            }
        }
    }
}
