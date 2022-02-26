package dev.madetobuild.typedconfig.runtime.source

import dev.madetobuild.typedconfig.runtime.EPSILON
import dev.madetobuild.typedconfig.runtime.ParseException
import dev.madetobuild.typedconfig.runtime.key.SimpleKey
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
internal class SystemPropertySourceTest {
    private val subject by lazy { SystemPropertySource() }

    @ParameterizedTest
    @MethodSource("successfulValues")
    fun successes(type: String, inputKey: String, envValue: String?, parsedValue: Any?) {
        if (envValue != null) {
            System.setProperty(inputKey, envValue)
        } else {
            System.clearProperty(inputKey)
        }

        when (type) {
            "string" -> subject.getString(SimpleKey(inputKey)) shouldBe parsedValue
            "int" -> subject.getInt(SimpleKey(inputKey)) shouldBe parsedValue
            "double" -> {
                val matcher = if (parsedValue == null) null else (parsedValue as Double plusOrMinus EPSILON)
                subject.getDouble(SimpleKey(inputKey)) shouldBe matcher
            }
            "bool" -> subject.getBoolean(SimpleKey(inputKey)) shouldBe parsedValue
            "list" -> subject.getList(SimpleKey(inputKey)) shouldBe parsedValue
            else -> throw UnsupportedOperationException(type)
        }
    }

    @ParameterizedTest
    @MethodSource("parseFailureValues")
    fun parseFailures(type: String, inputKey: String, envValue: String?, msgTail: String) {
        if (envValue != null) {
            System.setProperty(inputKey, envValue)
        } else {
            System.clearProperty(inputKey)
        }

        shouldThrow<ParseException> {
            when (type) {
                "string" -> subject.getString(SimpleKey(inputKey))
                "int" -> subject.getInt(SimpleKey(inputKey))
                "double" -> subject.getDouble(SimpleKey(inputKey))
                "bool" -> subject.getBoolean(SimpleKey(inputKey))
                "list" -> subject.getList(SimpleKey(inputKey))
                else -> throw UnsupportedOperationException(type)
            }
        } should { ex ->
            ex.shouldHaveMessage("Parse failure on key `$inputKey`: $msgTail")
        }
    }

    companion object {
        @JvmStatic
        fun successfulValues(): Array<Array<Any?>> = arrayOf(
            arrayOf("string", "foo", "bar", "bar"),
            arrayOf("string", "fooBarBaz", "bar", "bar"),
            arrayOf("string", "foo", null, null),
            arrayOf("int", "foo", "42", 42),
            arrayOf("int", "foo", null, null),
            arrayOf("double", "foo", "1.5", 1.5),
            arrayOf("double", "foo", null, null),
            arrayOf("bool", "foo", "true", true),
            arrayOf("bool", "foo", "false", false),
            arrayOf("bool", "foo", null, null),
            arrayOf("list", "foo", "single", listOf("single")),
            arrayOf("list", "foo", "several,elements,long", listOf("several", "elements", "long")),
            arrayOf("list", "foo", "spaces ,\tget, trimmed \n", listOf("spaces", "get", "trimmed")),
            arrayOf("list", "foo", "", emptyList<String>()),
            arrayOf("list", "foo", null, null),
        )

        @JvmStatic
        fun parseFailureValues(): Array<Array<Any?>> = arrayOf(
            arrayOf("int", "foo", "bar", "Not an integer: 'bar'"),
            arrayOf("double", "foo", "bar", "Not a double: 'bar'"),
            arrayOf("bool", "foo", "bar", "Not a boolean: 'bar'")
        )
    }
}
