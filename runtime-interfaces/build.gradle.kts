plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.mockk:mockk:1.12.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
