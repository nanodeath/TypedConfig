package dev.madetobuild.typedconfig

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

interface TypedConfigFile {
    val name: String
    val file: RegularFileProperty
}

interface TypedConfigExtension {
    val configs: NamedDomainObjectContainer<TypedConfigFile>
}

const val DEFAULT_CONFIG_FILENAME = "config.tc.toml"

class TypedConfigPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val pluginProperties = PluginProperties.fromPropertiesFile()
        val versions = Versions(pluginProperties)

        val extension = project.extensions.create("typedConfig", TypedConfigExtension::class.java)

        val configuration = project.configurations.create("typedConfigCodegen")
        project.dependencies.add(configuration.name, versions.codegenDependency)
        project.plugins.withType(JavaPlugin::class.java) {
            project.dependencies.add(
                JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, versions.runtimeDependency
            )
        }
        val generatedSourcesDir = project.layout.buildDirectory.dir("generated-config")
        project.plugins.withType(IdeaPlugin::class.java) {
            project.extensions.getByType(IdeaModel::class.java).module { module ->
                module.generatedSourceDirs.add(generatedSourcesDir.get().asFile)
            }
        }
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        val javaSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME).java
        javaSourceSet.srcDir(generatedSourcesDir)

        // Register umbrella task eagerly so compile tasks can depend on it
        val generateTypedConfigsTask = project.tasks.register("generateTypedConfigs") { t ->
            t.group = "typed config"
        }

        // Wire compile tasks to depend on code generation before afterEvaluate.
        // AbstractCompile covers Java; Kotlin 2.x's KotlinCompile no longer extends it.
        project.tasks.withType(AbstractCompile::class.java).configureEach { t ->
            t.dependsOn(generateTypedConfigsTask)
        }
        project.pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
            project.tasks.named("compileKotlin") { t ->
                t.dependsOn(generateTypedConfigsTask)
            }
        }

        project.afterEvaluate {
            val configs = extension.configs.takeUnless { it.isEmpty() }?.asMap ?: mapOf(
                "default" to buildDefaultTypedConfigFile(project)
            )
            val tasks: List<TaskProvider<JavaExec>> = configs.map { (name, config) ->
                val taskName = "generate${name.replaceFirstChar { it.uppercase() }}TypedConfig"
                project.tasks.register(taskName, JavaExec::class.java) { t ->
                    t.group = "typed config"
                    val inputFile = config.file.get()
                    t.inputs.file(inputFile)
                    t.outputs.dir(generatedSourcesDir)
                    t.classpath = configuration
                    t.mainClass.set("dev.madetobuild.typedconfig.codegen.TypedConfigCliKt")
                    t.args(inputFile, generatedSourcesDir.get())
                }
            }
            generateTypedConfigsTask.configure { t ->
                t.dependsOn(tasks)
            }
        }
    }

    private fun buildDefaultTypedConfigFile(
        project: Project
    ): TypedConfigFile {
        val objects = project.objects
        return object : TypedConfigFile {
            override val name = "default"
            override val file
                get() = objects.fileProperty().value(project.layout.projectDirectory.file(DEFAULT_CONFIG_FILENAME))
        }
    }
}
