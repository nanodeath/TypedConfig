package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.time.Duration

class DurationKeyTest {
    @ParameterizedTest
    @MethodSource("parseExpectations")
    fun parseDuration(value: String, expected: Duration) {
        value.asClue {
            DurationKey.parse(value) shouldBe expected
        }
    }

    @Test
    fun invalidDuration() {
        shouldThrow<ParseException> {
            DurationKey.parse("hello")
        }
    }

    companion object {
        @JvmStatic
        fun parseExpectations(): Array<Array<Any>> {
            val day = Duration.ofDays(1L)
            return listOf(
                "${day.toMillis()} milliseconds",
                "${day.toMillis()} millisecond",
                "${day.toMillis()}ms",
                "${day.toMinutes()} minutes",
                "${day.toMinutes()} minute",
                "${day.toMinutes()}m",
                "${day.toHours()} hours",
                "${day.toHours()} hour",
                "${day.toHours()}h",
                "${day.toDays()} days",
                "${day.toDays()} day",
                "${day.toDays()}d"
            )
                .map { arrayOf<Any>(it, day) }
                .toTypedArray()
        }
    }
}
