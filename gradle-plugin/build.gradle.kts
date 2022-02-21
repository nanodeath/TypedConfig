import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("TypedConfigPlugin") {
            id = "com.github.nanodeath.typedconfig"
            implementationClass = "com.github.nanodeath.typedconfig.TypedConfigPlugin"
        }
    }
}

java {
    withSourcesJar()
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
        path.writeText(properties.entries.joinToString("\n") { (k, v) -> "$k=$v"} )
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
