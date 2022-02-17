plugins {
    kotlin("jvm")
    idea
}

group = "com.github.nanodeath.typedconfig"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":generator-test-config"))
    implementation(project(":runtime"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.mockk:mockk:1.12.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named("compileTestKotlin").configure {
    dependsOn(":generator-test-config:generateConfigs")
}

val generatedSourcesTest = project(":generator-test-config").buildDir.resolve("generated-sources-test")
sourceSets {
    test {
        java {
            srcDir(generatedSourcesTest)
        }
    }
}

idea {
    module {
        generatedSourceDirs.add(generatedSourcesTest)
    }
}
