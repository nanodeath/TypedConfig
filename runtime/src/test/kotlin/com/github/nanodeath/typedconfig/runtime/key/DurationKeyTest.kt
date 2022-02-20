package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import io.kotest.assertions.forEachAsClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test
import java.time.Duration

class DurationKeyTest {
    @Test
    fun parseDuration() {
        val day = Duration.ofDays(1L)
        val dayInVariousFormats = listOf(
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

        dayInVariousFormats.forEachAsClue { durationString ->
            DurationKey.parseDuration(durationString, "key") shouldBe day
        }
    }

    @Test
    fun invalidDuration() {
        shouldThrow<ParseException> {
            DurationKey.parseDuration("hello", "key")
        } should {
            it.shouldHaveMessage("Failed to parse `key`: invalid duration: 'hello'")
        }
    }
}
