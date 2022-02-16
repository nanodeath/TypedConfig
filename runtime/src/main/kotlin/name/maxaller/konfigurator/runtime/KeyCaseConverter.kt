package name.maxaller.konfigurator.runtime

internal fun String.camelToUpperSnake(): String {
    val str = this
    return buildString((this.length * 1.25).toInt()) {
        for (idx in str.indices) {
            val char = str[idx]
            if (char != '.') {
                append(char.uppercase())
            }
            val next = str.getOrNull(idx + 1) ?: break
            if (Character.isUpperCase(next) && Character.isLowerCase(char) || char == '.') {
                append('_')
            }
        }
    }
}
