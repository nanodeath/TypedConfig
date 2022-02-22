plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

dependencies {
    api(project(":runtime-interfaces"))
    api("javax.annotation:javax.annotation-api:1.3.2")
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
                name.set("TypedConfig Runtime")
                description.set("Runtime required to compile and deploy TypedConfig-generated configuration")
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
