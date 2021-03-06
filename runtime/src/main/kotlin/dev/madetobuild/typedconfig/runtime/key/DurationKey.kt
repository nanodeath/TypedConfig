package dev.madetobuild.typedconfig.runtime.key

import dev.madetobuild.typedconfig.runtime.MissingConfigurationException
import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.source.Source
import java.time.Duration
import java.time.format.DateTimeParseException

class DurationKey(
    override val name: String,
    private val source: Source,
    default: String?,
    @Suppress("unused") private val checks: List<Unit>
) : Key<Duration> {
    private val parsedDefault: Duration? = default?.let { parse(it) }

    override fun resolve(): Duration =
        source.getString(this)?.let { parseWithName(it, name) }
            ?: parsedDefault
            ?: throw MissingConfigurationException(name)

    companion object : KeyObject<Duration> {
        private val longRegex = Regex("(?<count>\\d+) (?<unit>milliseconds?|seconds?|minutes?|hours?|days?)")
        private val shortRegex = Regex("(?<count>\\d+)(?<unit>ms|s|m|h|d)")

        override fun parse(value: String): Duration = try {
            Duration.parse(value)
        } catch (e: DateTimeParseException) {
            val (count, unit) = (longRegex.matchEntire(value)?.let { match ->
                match.groups["count"]!!.value.toLong() to match.groups["unit"]!!.value
            } ?: shortRegex.matchEntire(value)?.let { match ->
                match.groups["count"]!!.value.toLong() to match.groups["unit"]!!.value
            }) ?: throw ParseException("Not a valid duration: '$value'")
            when (unit) {
                "ms", "millisecond", "milliseconds" -> Duration.ofMillis(count)
                "s", "second", "seconds" -> Duration.ofSeconds(count)
                "m", "minute", "minutes" -> Duration.ofMinutes(count)
                "h", "hour", "hours" -> Duration.ofHours(count)
                "d", "day", "days" -> Duration.ofDays(count)
                // This matcher is actually exhaustive, so this line shouldn't even be reachable.
                else -> throw UnsupportedOperationException("Unexpected unit $unit, probably a TypedConfig bug")
            }
        }
    }
}
