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
        }
    }
}

addSonatypeRepository()

signing {
    if (!IsCI) {
        sign(publishing.publications["maven"])
    }
}
