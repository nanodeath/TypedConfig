plugins {
    kotlin("jvm")
    idea
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("reflect"))
    testImplementation(project(":codegen-test-config"))
    testImplementation(project(":runtime"))

    testImplementation(libs.bundles.tests)
    testImplementation(project(":runtime-test-common"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named("compileTestKotlin").configure {
    dependsOn(":codegen-test-config:generateConfigs")
}

val generatedSourcesTest = project(":codegen-test-config").layout.buildDirectory.dir("generated-sources-test").get().asFile
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
