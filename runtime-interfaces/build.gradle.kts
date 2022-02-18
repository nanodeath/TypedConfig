plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
