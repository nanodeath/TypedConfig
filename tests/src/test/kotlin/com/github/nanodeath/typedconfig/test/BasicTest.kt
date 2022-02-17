package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import org.junit.jupiter.api.Test

class BasicTest {
    @Test
    fun sanity() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 5

        val loginTries = GeneratedConfig(source).maxLoginTries
        loginTries shouldBe 5

        verifyAll { source.getInt("maxLoginTries") }
    }

    @Test
    fun stringKeyTest() {
        val source = mockk<Source>()
        every { source.getString(any()) } returns "hello"

        val nameOfTestUser = GeneratedConfig(source).nameOfTestUser
        nameOfTestUser shouldBe "hello"

        verifyAll { source.getString("nameOfTestUser") }
    }

    @Test
    fun missingRequiredConfigTest() {
        val source = mockk<Source>()
        every { source.getString(any()) } returns null

        shouldThrow<MissingConfigurationException> {
            GeneratedConfig(source).requiredConfig
        } should { ex ->
            ex.message shouldBe "Config `requiredConfig` is required " +
                    "but could not be resolved and no default was provided"
        }

        verifyAll { source.getString("requiredConfig") }
    }
}
