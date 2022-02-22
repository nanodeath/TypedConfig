plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":runtime-interfaces"))
    implementation("com.fasterxml.jackson.jr:jackson-jr-objects:${libs.versions.jackson.get()}")
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    withJavadocJar()
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

addSonatypeRepository()

signing {
    if (!IsCI) {
        sign(publishing.publications["maven"])
    }
}
