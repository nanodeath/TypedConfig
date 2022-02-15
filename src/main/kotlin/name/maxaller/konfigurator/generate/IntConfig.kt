package name.maxaller.konfigurator.generate

data class IntConfig(
    val key: String,
    val defaultValue: Int?,
    val constraints: List<String>?
)
