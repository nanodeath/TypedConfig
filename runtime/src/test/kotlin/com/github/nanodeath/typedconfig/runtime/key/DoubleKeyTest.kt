package com.github.nanodeath.typedconfig.runtime.key

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class DoubleKeyTest {
    @ParameterizedTest
    @MethodSource("parseExpectations")
    fun parseDouble(value: String, expected: Double) {
        DoubleKey.parse(value) shouldBe (expected.plusOrMinus(0.00001))
    }

    @Test
    fun emptyString() {
        shouldThrow<NumberFormatException> {
            DoubleKey.parse("")
        }
    }

    companion object {
        @JvmStatic
        fun parseExpectations(): Array<Array<Any>> =
            listOf(
                "0.0" to 0.0,
                "1.0" to 1.0,
                "-1.0" to -1.0,
                "1" to 1
            )
                .map { (value, expected) -> arrayOf<Any>(value, expected) }
                .toTypedArray()
    }
}
