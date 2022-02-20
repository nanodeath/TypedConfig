package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source
import java.time.Duration
import java.time.format.DateTimeParseException

class DurationKey(
    private val name: String,
    private val source: Source,
    private val default: String?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Duration> {
    private val parsedDefault: Duration? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        default?.let { parse(it) }
    }

    override fun resolve(): Duration =
        source.getString(name)?.let {
            try {
                parse(it)
            } catch (e: ParseException) {
                throw ParseException(name, "invalid duration: '$it'")
            }
        }
            ?: parsedDefault
            ?: throw MissingConfigurationException(name)

    companion object : KeyObject<Duration> {
        private val longRegex = Regex("(?<count>\\d+) (?<unit>milliseconds?|minutes?|hours?|days?)")
        private val shortRegex = Regex("(?<count>\\d+)(?<unit>ms|m|h|d)")

        override fun parse(value: String): Duration = try {
            Duration.parse(value)
        } catch (e: DateTimeParseException) {
            val (count, unit) = (longRegex.matchEntire(value)?.let { match ->
                match.groups["count"]!!.value.toLong() to match.groups["unit"]!!.value
            } ?: shortRegex.matchEntire(value)?.let { match ->
                match.groups["count"]!!.value.toLong() to match.groups["unit"]!!.value
            }) ?: throw ParseException()
            when (unit) {
                "ms", "millisecond", "milliseconds" -> Duration.ofMillis(count)
                "m", "minute", "minutes" -> Duration.ofMinutes(count)
                "h", "hour", "hours" -> Duration.ofHours(count)
                "d", "day", "days" -> Duration.ofDays(count)
                // This matcher is actually exhaustive, so this line shouldn't even be reachable.
                else -> throw UnsupportedOperationException("Unexpected unit $unit, probably a TypedConfig bug")
            }
        }
    }
}
