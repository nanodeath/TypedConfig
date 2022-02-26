package dev.madetobuild.typedconfig.runtime.source

import com.fasterxml.jackson.jr.ob.JSON
import dev.madetobuild.typedconfig.runtime.key.SimpleKey
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
        source.getInt(SimpleKey("foo")) shouldBe 1
    }

    @Test
    fun intMissingTopLevelKey() {
        source.getInt(SimpleKey("missing")) should beNull()
    }

    @Test
    fun intNestedKey() {
        source.getInt(SimpleKey("bar.baz")) shouldBe 2
    }

    @Test
    fun intNestedKeyObject() {
        source.getInt(SimpleKey("bar")) should beNull()
    }

    @Test
    fun intNestedKeyList() {
        source.getInt(SimpleKey("list.1")) should beNull()
    }

    @Test
    fun stringKey() {
        source.getString(SimpleKey("str")) shouldBe "foo"
        source.getString(SimpleKey("foo")) shouldBe null
    }

    @Test
    fun doubleKey() {
        source.getDouble(SimpleKey("double")) shouldBe (1.23 plusOrMinus 0.01)
        source.getDouble(SimpleKey("foo")) shouldBe null
    }

    @Test
    fun booleanKey() {
        source.getBoolean(SimpleKey("boolean")) shouldBe true
        source.getBoolean(SimpleKey("unknown")) shouldBe null
        shouldThrow<ClassCastException> {
            // Hmm...
            source.getBoolean(SimpleKey("foo"))
        }
    }

    @Test
    fun parseFile() {
        val file = Files.createTempFile("TypedConfig.", ".json").toFile()
        try {
            JSON.std.write(mapOf("foo" to "bar"), file)
            JsonSource(file).getString(SimpleKey("foo")) shouldBe "bar"
        } finally {
            Files.deleteIfExists(file.toPath())
        }
    }
}
