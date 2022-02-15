package name.maxaller.konfigurator.runtime

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
}
