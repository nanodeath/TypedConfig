package dev.madetobuild.typedconfig.codegen

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

internal class SuggesterTest {
    @Test
    fun exactMatch() {
        shouldThrow<IllegalArgumentException> {
            Suggester(listOf("foo", "bar", "baz")).suggest("bar")
        } should { ex ->
            ex.shouldHaveMessage("Unrecognized top-level key: `bar`. Did you mean one of these? `bar`, `baz`, `foo`")
        }
    }

    @Test
    fun nearMiss() {
        shouldThrow<IllegalArgumentException> {
            Suggester(listOf("foo", "bar", "baz")).suggest("raz")
        } should { ex ->
            ex.shouldHaveMessage("Unrecognized top-level key: `raz`. Did you mean one of these? `baz`, `bar`, `foo`")
        }
    }

    @Test
    fun notEvenClose() {
        shouldThrow<IllegalArgumentException> {
            Suggester(listOf("foo", "bar", "baz")).suggest("potato")
        } should { ex ->
            ex.shouldHaveMessage("Unrecognized top-level key: `potato`. Did you mean one of these? `bar`, `baz`, `foo`")
        }
    }
}
