package name.maxaller.konfigurator

data class IntConfig(
    val key: String,
    val defaultValue: Int?,
    val constraints: List<String>?
)
