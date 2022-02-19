plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    api(project(":runtime-interfaces"))
    api("javax.annotation:javax.annotation-api:1.3.2")
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
