package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.ParseException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class BooleanKeyTest {
    @ParameterizedTest
    @MethodSource("trueValues")
    fun parsesTrueCorrectly(value: String) {
        BooleanKey.parse(value) shouldBe true
    }

    @ParameterizedTest
    @MethodSource("falseValues")
    fun parsesFalseCorrectly(value: String) {
        BooleanKey.parse(value) shouldBe false
    }

    @Test
    fun emptyIsNotFalse() {
        shouldThrow<ParseException> {
            BooleanKey.parse("")
        }
    }

    companion object {
        @JvmStatic
        fun trueValues() = listOf(
            "true",
            "TRUE",
            "True"
        )

        @JvmStatic
        fun falseValues() = listOf(
            "false",
            "FALSE",
            "False"
        )
    }
}
