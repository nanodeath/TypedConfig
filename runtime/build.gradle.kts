plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":runtime-interfaces"))
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
