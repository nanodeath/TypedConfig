package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
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

    @Test
    fun parseFailureInResolve() {
        val source = mockk<Source>()
        every { source.getString(any()) } returns "a few seconds"

        val durationKey = DurationKey("timeout", source, null, emptyList())
        shouldThrow<ParseException> {
            durationKey.resolve()
        } should { e ->
            e.shouldHaveMessage("Parse failure on key `timeout`: Not a valid duration: 'a few seconds'")
        }

        verifyAll { source.getString(durationKey) }
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
