plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":runtime-interfaces"))
    implementation(libs.bundles.tests)
}
