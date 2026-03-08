plugins {
    kotlin("jvm")
    idea
    id("typedconfig.published-conventions")
}

val testCompiler: Configuration = configurations.create("testCompiler")

dependencies {
    implementation(kotlin("reflect"))
    api(project(":runtime-interfaces"))
    api(libs.koin.core)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit5)
    testImplementation(libs.bundles.tests)
    testImplementation(project(":codegen"))
    testImplementation(project(":runtime"))
    testCompiler(project(":codegen-test-config"))
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("TypedConfig Koin Module")
                description.set("Koin dependency injection integration for TypedConfig")
            }
        }
    }
}

val generatedSourcesTest = project.layout.buildDirectory.dir("generated-sources-test").get().asFile
val generateConfigs = tasks.register<JavaExec>("generateConfigs") {
    group = "verification"
    classpath = testCompiler
    mainClass.set("TestCompilerKt")

    val inputDir = project.projectDir.resolve("src/testConfig")
    val outputDir = generatedSourcesTest
    inputs.dir(inputDir)
    outputs.dir(outputDir)
    args(inputDir, outputDir)
}

tasks.withType(AbstractCompile::class) {
    dependsOn(generateConfigs)
}

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
