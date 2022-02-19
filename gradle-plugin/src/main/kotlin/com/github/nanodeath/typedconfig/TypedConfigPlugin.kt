package com.github.nanodeath.typedconfig

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

class TypedConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val configuration = project.configurations.create("typedConfigGenerator")
        project.dependencies.add(configuration.name, "com.github.nanodeath.typedconfig:generator:1.0-SNAPSHOT")
        project.plugins.withType(JavaPlugin::class.java) {
            project.dependencies.add(
                JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, "com.github.nanodeath.typedconfig:runtime:1.0-SNAPSHOT"
            )
        }
        val generatedSourcesDir = project.buildDir.resolve("generated-config")
        project.plugins.withType(IdeaPlugin::class.java) {
            project.extensions.getByType(IdeaModel::class.java).module { module ->
                module.generatedSourceDirs.add(generatedSourcesDir)
            }
        }
        project.tasks.register("generateTypedConfigClasses", JavaExec::class.java) { t ->
            t.group = "build"
            val inputFile = "config.tc.toml"
            t.inputs.file(inputFile)
            t.outputs.dir(generatedSourcesDir)
            t.classpath = configuration
            t.mainClass.set("com.github.nanodeath.typedconfig.generate.Generator")
            t.args(inputFile, generatedSourcesDir)
        }
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        val javaSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java
        javaSourceSet.srcDir(generatedSourcesDir)
    }
}
