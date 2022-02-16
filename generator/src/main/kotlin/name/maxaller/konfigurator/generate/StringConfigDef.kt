package name.maxaller.konfigurator.generate

data class StringConfigDef(
    val key: String,
    val defaultValue: String?,
    val constraints: List<String>
) : ConfigDef
