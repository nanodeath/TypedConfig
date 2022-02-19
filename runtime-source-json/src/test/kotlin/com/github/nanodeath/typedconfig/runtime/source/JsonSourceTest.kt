package com.github.nanodeath.typedconfig.runtime.source

import com.fasterxml.jackson.jr.ob.JSON
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Files

class JsonSourceTest {
    private val source = JsonSource(
        mapOf(
            "foo" to 1,
            "bar" to mapOf("baz" to 2),
            // lists don't make sense here
            "list" to listOf(3),
            "str" to "foo",
            "double" to 1.23,
            "boolean" to true,
        )
    )

    @Test
    fun intTopLevelKey() {
        source.getInt("foo") shouldBe 1
    }

    @Test
    fun intMissingTopLevelKey() {
        source.getInt("missing") should beNull()
    }

    @Test
    fun intNestedKey() {
        source.getInt("bar.baz") shouldBe 2
    }

    @Test
    fun intNestedKeyObject() {
        source.getInt("bar") should beNull()
    }

    @Test
    fun intNestedKeyList() {
        source.getInt("list.1") should beNull()
    }

    @Test
    fun stringKey() {
        source.getString("str") shouldBe "foo"
        source.getString("foo") shouldBe null
    }

    @Test
    fun doubleKey() {
        source.getDouble("double") shouldBe (1.23 plusOrMinus 0.01)
        source.getDouble("foo") shouldBe null
    }

    @Test
    fun booleanKey() {
        source.getBoolean("boolean") shouldBe true
        source.getBoolean("unknown") shouldBe null
        shouldThrow<ClassCastException> {
            // Hmm...
            source.getBoolean("foo")
        }
    }

    @Test
    fun parseFile() {
        val file = Files.createTempFile("TypedConfig.", ".json").toFile()
        try {
            JSON.std.write(mapOf("foo" to "bar"), file)
            JsonSource(file).getString("foo") shouldBe "bar"
        } finally {
            Files.deleteIfExists(file.toPath())
        }
    }
}
