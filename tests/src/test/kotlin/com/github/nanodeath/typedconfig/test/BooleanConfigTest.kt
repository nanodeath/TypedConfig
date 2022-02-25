package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.key.keyWithName
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
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
class BooleanConfigTest {
    @MockK
    lateinit var source: Source

    @Test
    fun requiredBooleanWithValue() {
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).requiredBoolean shouldBe true

        verifyAll { source.getBoolean(keyWithName("requiredBoolean")) }
    }

    @Test
    fun requiredBooleanWithoutValue() {
        every { source.getBoolean(any()) } returns null

        shouldThrow<MissingConfigurationException> {
            BooleanConfig(source).requiredBoolean
        }

        verifyAll { source.getBoolean(keyWithName("requiredBoolean")) }
    }

    @Test
    fun optionalBooleanWithValue() {
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).optionalBoolean shouldBe true

        verifyAll { source.getBoolean(keyWithName("optionalBoolean")) }
    }

    @Test
    fun optionalBooleanWithoutValue() {
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).optionalBoolean should beNull()

        verifyAll { source.getBoolean(keyWithName("optionalBoolean")) }
    }

    @Test
    fun defaultTrueWithValue() {
        every { source.getBoolean(any()) } returns false

        BooleanConfig(source).defaultTrue shouldBe false

        verifyAll { source.getBoolean(keyWithName("defaultTrue")) }
    }

    @Test
    fun defaultTrueWithoutValue() {
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).defaultTrue shouldBe true

        verifyAll { source.getBoolean(keyWithName("defaultTrue")) }
    }

    @Test
    fun defaultFalseWithValue() {
        every { source.getBoolean(any()) } returns true

        BooleanConfig(source).defaultFalse shouldBe true

        verifyAll { source.getBoolean(keyWithName("defaultFalse")) }
    }

    @Test
    fun defaultFalseWithoutValue() {
        every { source.getBoolean(any()) } returns null

        BooleanConfig(source).defaultFalse shouldBe false

        verifyAll { source.getBoolean(keyWithName("defaultFalse")) }
    }
}
