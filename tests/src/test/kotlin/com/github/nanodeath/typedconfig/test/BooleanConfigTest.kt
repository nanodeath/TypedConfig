package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

class BooleanConfigTest {
    @Test
    fun requiredBooleanWithValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).requiredBoolean shouldBe true

        verifyAll { source.getBoolean("requiredBoolean") }
    }

    @Test
    fun requiredBooleanWithoutValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns null

        shouldThrow<MissingConfigurationException> {
            BooleanConfig(source).requiredBoolean
        }

        verifyAll { source.getBoolean("requiredBoolean") }
    }

    @Test
    fun optionalBooleanWithValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).optionalBoolean shouldBe true

        verifyAll { source.getBoolean("optionalBoolean") }
    }

    @Test
    fun optionalBooleanWithoutValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).optionalBoolean should beNull()

        verifyAll { source.getBoolean("optionalBoolean") }
    }

    @Test
    fun defaultTrueWithValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns false

        BooleanConfig(source).defaultTrue shouldBe false

        verifyAll { source.getBoolean("defaultTrue") }
    }

    @Test
    fun defaultTrueWithoutValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).defaultTrue shouldBe true

        verifyAll { source.getBoolean("defaultTrue") }
    }

    @Test
    fun defaultFalseWithValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).defaultFalse shouldBe true

        verifyAll { source.getBoolean("defaultFalse") }
    }

    @Test
    fun defaultFalseWithoutValue() {
        val source = mockk<Source>()
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).defaultFalse shouldBe false

        verifyAll { source.getBoolean("defaultFalse") }
    }
}
