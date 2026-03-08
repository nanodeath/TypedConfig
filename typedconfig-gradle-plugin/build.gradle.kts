plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    alias(libs.plugins.gradle.plugin.publish)
    id("typedconfig.published-conventions")
}

dependencies {
    testImplementation(libs.bundles.tests)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

gradlePlugin {
    website = "https://github.com/nanodeath/TypedConfig"
    vcsUrl = "https://github.com/nanodeath/TypedConfig"

    plugins {
        create("TypedConfigPlugin") {
            id = group.toString()
            tags = listOf("config", "kotlin")
            displayName = "TypedConfig Gradle Plugin"
            description = "Automatically configure generator and runtime for TypedConfig, a strongly-typed " +
                    "configuration library"
            implementationClass = "dev.madetobuild.typedconfig.TypedConfigPlugin"
        }
    }
}

val generatedResourcesDirectory = layout.buildDirectory.dir("generated-resources").get().asFile
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
    listOf("processResources", "sourcesJar", "publishPlugins").forEach { taskName ->
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
