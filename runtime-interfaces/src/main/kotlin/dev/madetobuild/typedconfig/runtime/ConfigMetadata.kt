package dev.madetobuild.typedconfig.runtime

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigMetadata(
    val description: String = "",
    val namespace: String = "",
    val sourceFile: String = ""
)
