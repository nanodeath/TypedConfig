plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

dependencies {
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
                name.set("TypedConfig Runtime Interfaces")
                description.set("Simple interfaces used by TypedConfig plugins")
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
