package dev.madetobuild.typedconfig.runtime

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CaseConverterTest {
    @Test
    fun lowerCamelCase() {
        "lowerCamelCase".camelToUpperSnake() shouldBe "LOWER_CAMEL_CASE"
    }

    @Test
    fun upperCamelCase() {
        "UpperCamelCase".camelToUpperSnake() shouldBe "UPPER_CAMEL_CASE"
    }

    @Test
    fun lowerCamelCaseWithDots() {
        "app.port".camelToUpperSnake() shouldBe "APP_PORT"
    }

    @Test
    fun lowerCamelCaseWordsWithDots() {
        "app.securePort".camelToUpperSnake() shouldBe "APP_SECURE_PORT"
    }
}
