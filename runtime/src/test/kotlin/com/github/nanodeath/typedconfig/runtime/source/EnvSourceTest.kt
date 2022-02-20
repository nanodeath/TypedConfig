package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.EPSILON
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class EnvSourceTest {
    @MockK
    lateinit var env: Env

    private val subject by lazy { EnvSource(env) }

    @Test
    fun getStringTest() {
        every { env.get(any()) } returns "bar"

        subject.getString("foo") shouldBe "bar"

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getStringCaseTest() {
        every { env.get(any()) } returns "bar"

        subject.getString("fooBarBaz") shouldBe "bar"

        verifyAll { env.get("FOO_BAR_BAZ") }
    }

    @Test
    fun getIntTest() {
        every { env.get(any()) } returns "42"

        subject.getInt("foo") shouldBe 42

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getInvalidStringTest() {
        every { env.get(any()) } returns "bar"

        // TODO this should throw
        subject.getInt("foo") should beNull()

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getDoubleTest() {
        every { env.get(any()) } returns "1.5"

        subject.getDouble("foo") shouldBe (1.5 plusOrMinus EPSILON)

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getInvalidDoubleTest() {
        every { env.get(any()) } returns "bar"

        // TODO this should throw
        subject.getDouble("foo") should beNull()

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getBooleanTest() {
        every { env.get(any()) } returns "true"

        subject.getBoolean("foo") shouldBe true

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getInvalidBooleanTest() {
        every { env.get(any()) } returns "bar"

        // TODO this should throw
        subject.getBoolean("foo") shouldBe false

        verifyAll { env.get("FOO") }
    }
}
