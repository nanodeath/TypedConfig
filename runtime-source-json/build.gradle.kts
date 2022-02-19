plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(project(":runtime-interfaces"))
    implementation("com.fasterxml.jackson.jr:jackson-jr-objects:${libs.versions.jackson.get()}")
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
