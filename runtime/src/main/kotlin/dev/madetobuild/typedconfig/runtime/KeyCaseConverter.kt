package dev.madetobuild.typedconfig.runtime

// Random guess at how much longer a string needs to be to accommodate new underscores
private const val UNDERSCORE_FACTOR = 1.25

internal fun String.camelToUpperSnake(): String {
    val str = this
    return buildString((this.length * UNDERSCORE_FACTOR).toInt()) {
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
