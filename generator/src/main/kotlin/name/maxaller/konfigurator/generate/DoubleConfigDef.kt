package name.maxaller.konfigurator.generate

internal data class DoubleConfigDef(
    val key: String,
    val defaultValue: Double?,
    val constraints: List<String>
) : ConfigDef
