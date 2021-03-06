plugins {
    kotlin("jvm")
    id("typedconfig.published-conventions")
}

dependencies {
    api(project(":runtime-interfaces"))
    api("javax.annotation:javax.annotation-api:1.3.2")
    implementation(libs.slf4j)
    testImplementation(libs.bundles.tests)
    testImplementation(project(":runtime-test-common"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    systemProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn")
    systemProperty("org.slf4j.simpleLogger.log.dev.madetobuild.typedconfig", "debug")
    systemProperty("org.slf4j.simpleLogger.showThreadName", "false")
    systemProperty("org.slf4j.simpleLogger.showShortLogName", "true")
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
