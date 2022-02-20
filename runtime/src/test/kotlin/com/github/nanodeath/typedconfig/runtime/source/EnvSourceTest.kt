package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.EPSILON
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
internal class EnvSourceTest {
    @MockK
    lateinit var env: Env

    private val subject by lazy { EnvSource(env) }

    @ParameterizedTest
    @MethodSource("successfulValues")
    fun successes(type: String, inputKey: String, outputKey: String, envValue: String?, parsedValue: Any?) {
        every { env.get(any()) } returns envValue

        when (type) {
            "string" -> subject.getString(inputKey) shouldBe parsedValue
            "int" -> subject.getInt(inputKey) shouldBe parsedValue
            "double" -> {
                val matcher = if (parsedValue == null) null else (parsedValue as Double plusOrMinus EPSILON)
                subject.getDouble(inputKey) shouldBe matcher
            }
            "bool" -> subject.getBoolean(inputKey) shouldBe parsedValue
            "list" -> subject.getList(inputKey) shouldBe parsedValue
            else -> throw UnsupportedOperationException(type)
        }

        verifyAll { env.get(outputKey) }
    }

    companion object {
        @JvmStatic
        fun successfulValues(): Array<Array<Any?>> = arrayOf(
            arrayOf("string", "foo", "FOO", "bar", "bar"),
            arrayOf("string", "fooBarBaz", "FOO_BAR_BAZ", "bar", "bar"),
            arrayOf("string", "foo", "FOO", null, null),
            arrayOf("int", "foo", "FOO", "42", 42),
            arrayOf("int", "foo", "FOO", "bar", null), // TODO should throw, #65
            arrayOf("int", "foo", "FOO", null, null),
            arrayOf("double", "foo", "FOO", "1.5", 1.5),
            arrayOf("double", "foo", "FOO", "bar", null), // TODO should throw, #65
            arrayOf("double", "foo", "FOO", null, null),
            arrayOf("bool", "foo", "FOO", "true", true),
            arrayOf("bool", "foo", "FOO", "false", false),
            arrayOf("bool", "foo", "FOO", "bar", false), // TODO should throw, #65
            arrayOf("bool", "foo", "FOO", null, null),
            arrayOf("list", "foo", "FOO", "single", listOf("single")),
            arrayOf("list", "foo", "FOO", "several,elements,long", listOf("several", "elements", "long")),
            arrayOf("list", "foo", "FOO", "spaces ,\tget, trimmed \n", listOf("spaces", "get", "trimmed")),
            arrayOf("list", "foo", "FOO", "", emptyList<String>()),
            arrayOf("list", "foo", "FOO", null, null),
        )
    }
}
