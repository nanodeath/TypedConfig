plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":codegen"))
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

val generateConfigs = tasks.register<JavaExec>("generateConfigs") {
    group = "Execution"
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("TestCompilerKt")

    val inputDir = project.projectDir.resolve("src/main/resources")
    val outputDir = project.buildDir.resolve("generated-sources-test")
    inputs.dir(inputDir)
    outputs.dir(outputDir)
    args(inputDir, outputDir)
}

tasks.named("assemble").configure {
    finalizedBy(generateConfigs)
}
