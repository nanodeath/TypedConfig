package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.EPSILON
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
        DoubleKey.parse(value) shouldBe (expected plusOrMinus EPSILON)
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
                "1" to 1,
                "1e3" to 1000
            )
                .map { (value, expected) -> arrayOf<Any>(value, expected) }
                .toTypedArray()
    }
}
