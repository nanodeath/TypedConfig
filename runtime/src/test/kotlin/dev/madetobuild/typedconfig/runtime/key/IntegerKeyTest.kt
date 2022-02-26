package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.ParseException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class IntegerKeyTest {
    @ParameterizedTest
    @MethodSource("parseExpectations")
    fun parseInteger(value: String, expected: Int) {
        IntegerKey.parse(value) shouldBe expected
    }


    @Test
    fun decimalString() {
        shouldThrow<ParseException> {
            IntegerKey.parse("1.0")
        }
    }

    @Test
    fun emptyString() {
        shouldThrow<ParseException> {
            IntegerKey.parse("")
        }
    }

    companion object {
        @JvmStatic
        fun parseExpectations(): Array<Array<Any>> =
            listOf(
                "1" to 1,
                "0" to 0,
                "-1" to -1,
                "1000000" to 1000000
            )
                .map { (value, expected) -> arrayOf<Any>(value, expected) }
                .toTypedArray()
    }
}
