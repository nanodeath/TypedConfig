plugins {
    kotlin("jvm")
    `maven-publish`
    signing
}

dependencies {
    api(project(":runtime-interfaces"))
    api("javax.annotation:javax.annotation-api:1.3.2")
    implementation(libs.slf4j)
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    systemProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn")
    systemProperty("org.slf4j.simpleLogger.log.com.github.nanodeath.typedconfig", "debug")
    systemProperty("org.slf4j.simpleLogger.showThreadName", "false")
    systemProperty("org.slf4j.simpleLogger.showShortLogName", "true")
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
