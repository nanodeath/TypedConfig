plugins {
    kotlin("jvm") version "1.6.10"
    application
}

group = "name.maxaller.konfigurator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // tool
    implementation(enforcedPlatform("com.fasterxml.jackson:jackson-bom:2.13.1"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml")
    implementation("com.squareup:kotlinpoet:1.10.2")

    // runtime
//    implementation("org.apache.commons:commons-text:1.9")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.mockk:mockk:1.12.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val generateConfigs = tasks.register<JavaExec>("generateStuff") {
    group = "Execution"
    description = "Run the main class with JavaExecTask"
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("TestCompilerKt")
}

sourceSets {
    test {
        java {
            srcDir("build/generated-sources-test")
        }
    }
}
