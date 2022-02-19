package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import java.io.File

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

    @Test
    fun descriptionIsAttachedToClass() {
        val sourceText =
            File("../generator-test-config/build/generated-sources-test/com/github/nanodeath/typedconfig/test/GeneratedConfig.kt")
                .readText()

        sourceText shouldContain "* This is the basic configuration file."
    }

    @Test
    fun descriptionIsAttachedToProperty() {
        val sourceText =
            File("../generator-test-config/build/generated-sources-test/com/github/nanodeath/typedconfig/test/GeneratedConfig.kt")
                .readText()

        // Well, we're at least checking that the file contains this comment somewhere.
        sourceText shouldContain "* Maximum number of login tries before locking account."
    }
}
