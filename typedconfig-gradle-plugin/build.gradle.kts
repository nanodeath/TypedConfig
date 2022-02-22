plugins {
    kotlin("jvm") version "1.6.10"
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.18.0"
    signing
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

gradlePlugin {
    plugins {
        create("TypedConfigPlugin") {
            id = "com.github.nanodeath.typedconfig"
            displayName = "TypedConfig Gradle Plugin"
            description = "Automatically configure generator and runtime for TypedConfig (a strongly-typed " +
                    "configuration library)"
            implementationClass = "com.github.nanodeath.typedconfig.TypedConfigPlugin"
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

val generatedResourcesDirectory = buildDir.resolve("generated-resources")
val generateResourcesTask = tasks.register("generateResources") {
    val properties = mapOf(
        "version" to rootProject.version
    )
    inputs.properties(properties)
    outputs.dir(generatedResourcesDirectory)
    outputs.files(fileTree(generatedResourcesDirectory))
    doLast {
        val path = generatedResourcesDirectory.resolve("com/github/nanodeath/typedconfig/plugin.properties")
        path.parentFile.mkdirs()
        path.writeText(properties.entries.joinToString("\n") { (k, v) -> "$k=$v" })
    }
}

sourceSets {
    main {
        resources {
            // unfortunately compiledBy doesn't seem to work here
//            compiledBy(generateResourcesTask)
            srcDir(generatedResourcesDirectory)
        }
    }
}

listOf("processResources", "sourcesJar").forEach { taskName ->
    tasks.getByName(taskName).dependsOn(generateResourcesTask)
}

addSonatypeRepository()

afterEvaluate {
    if (!IsCI) {
        signing {
            sign(publishing.publications["pluginMaven"])
        }
    }
}

pluginBundle {
    website = "https://github.com/nanodeath/TypedConfig"
    vcsUrl = "https://github.com/nanodeath/TypedConfig"
    tags = listOf("config", "kotlin")
}
