import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
//    `kotlin-dsl`
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

val generateSourcesTask = tasks.register<Sync>("generateSources") {
    val tokens = mapOf(
        "VERSION" to rootProject.version
    )
    inputs.properties(tokens)
    from("src/main/kotlin")
    into(buildDir.resolve("generated-src"))
    filter<ReplaceTokens>(mapOf("tokens" to tokens))
}

lateinit var releaseSourceSet: SourceSet
sourceSets {
    releaseSourceSet = create("release") {
        java {
            compiledBy(generateSourcesTask) { t ->
                objects.directoryProperty().also { it.set(t.destinationDir) }
            }
            srcDir(generateSourcesTask.map { it.destinationDir })
            compileClasspath = sourceSets.main.get().compileClasspath
            runtimeClasspath = sourceSets.main.get().runtimeClasspath
        }
    }
}

tasks.register<Jar>("releaseJar") {
    group = "build"
    archiveBaseName.set("${project.name}-release")
    from(releaseSourceSet.output)
}

tasks.register<Jar>("releaseSourcesJar") {
    group = "build"
    archiveClassifier.set("sources")
    archiveBaseName.set("${project.name}-release")
    from(releaseSourceSet.java.sourceDirectories)
}
