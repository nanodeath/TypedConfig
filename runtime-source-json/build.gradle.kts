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
        }
    }
}

addSonatypeRepository()

signing {
    sign(publishing.publications["maven"])
}
