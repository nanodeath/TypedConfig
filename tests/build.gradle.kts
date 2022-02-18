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
    testImplementation(kotlin("reflect"))
    testImplementation(project(":generator-test-config"))
    testImplementation(project(":runtime"))

    testImplementation(libs.bundles.tests)
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
