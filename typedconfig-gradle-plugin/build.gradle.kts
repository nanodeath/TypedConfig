plugins {
    kotlin("jvm") version "1.6.10"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.18.0"
    id("typedconfig.published-conventions")
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
            id = "$group.typedconfig"
            displayName = "TypedConfig Gradle Plugin"
            description = "Automatically configure generator and runtime for TypedConfig, a strongly-typed " +
                    "configuration library"
            implementationClass = "dev.madetobuild.typedconfig.TypedConfigPlugin"
        }
    }
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
        val path = generatedResourcesDirectory.resolve("dev/madetobuild/typedconfig/plugin.properties")
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

afterEvaluate {
    listOf("processResources", "sourcesJar", "publishPluginJar").forEach { taskName ->
        tasks.getByName(taskName).dependsOn(generateResourcesTask)
    }
    (publishing.publications["TypedConfigPluginPluginMarkerMaven"] as MavenPublication).apply {
        pom {
            name.set("TypedConfig Gradle Plugin Marker")
            description.set("Plugin marker for TypedConfig Gradle Plugin")
        }
    }
    (publishing.publications["pluginMaven"] as MavenPublication).apply {
        pom {
            name.set("TypedConfig Gradle Plugin")
            description.set(
                "Automatically configure generator and runtime for TypedConfig, a strongly-typed " +
                        "configuration library"
            )
        }
    }
}

pluginBundle {
    website = "https://github.com/nanodeath/TypedConfig"
    vcsUrl = "https://github.com/nanodeath/TypedConfig"
    tags = listOf("config", "kotlin")
}
