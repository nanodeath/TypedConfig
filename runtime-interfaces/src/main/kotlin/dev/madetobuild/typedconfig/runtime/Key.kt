package dev.madetobuild.typedconfig.runtime

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Key(
    val name: String,
    val description: String = "",
    val required: Boolean = true,
    val sensitive: Boolean = false,
    val defaultValue: String = "",
    val type: String = ""
)
