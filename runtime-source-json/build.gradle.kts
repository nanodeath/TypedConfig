plugins {
    kotlin("jvm")
    id("typedconfig.published-conventions")
}

dependencies {
    implementation(project(":runtime-interfaces"))
    implementation(libs.jackson.jr.objects)
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
