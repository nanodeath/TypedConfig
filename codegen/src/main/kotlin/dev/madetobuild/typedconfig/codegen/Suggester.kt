package dev.madetobuild.typedconfig.codegen

import org.apache.commons.text.similarity.LevenshteinDistance

internal class Suggester(private val options: Collection<String>) {
    private val distance = LevenshteinDistance(threshold)

    fun suggest(given: String): Nothing {
        val ideas = options.asSequence()
            .map { option -> DistanceTuple(option, distance.apply(given, option)) }
            // We want -1 values (over threshold) to sort last
            .map { (option, distance) -> DistanceTuple(option, (distance.takeIf { it >= 0 } ?: Int.MAX_VALUE)) }
            // Sort ascending by distance, then by option in case of tie
            .sortedWith(compareBy<DistanceTuple> { it.distance }.thenBy { it.value })
            .map { (option, _) -> option }
            .toList()
        throw IllegalArgumentException(
            "Unrecognized top-level key: `$given`. " +
                    "Did you mean one of these? ${ideas.joinToString(", ") { "`$it`" }}"
        )
    }

    private data class DistanceTuple(val value: String, val distance: Int)

    private companion object {
        /**
         * Some reasonably-low number for likely typos.
         */
        const val threshold = 3
    }
}
