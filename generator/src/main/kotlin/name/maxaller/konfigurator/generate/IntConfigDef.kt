package name.maxaller.konfigurator.generate

internal data class IntConfigDef(
    val key: String,
    val defaultValue: Int?,
    val constraints: List<String>
) : ConfigDef
