package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.File

@ExtendWith(MockKExtension::class)
class BasicTest {
    @MockK
    lateinit var source: Source

    @Test
    fun sanity() {
        every { source.getInt(any()) } returns 5

        val loginTries = GeneratedConfig(source).maxLoginTries
        loginTries shouldBe 5

        verifyAll { source.getInt("maxLoginTries") }
    }

    @Test
    fun stringKeyTest() {
        every { source.getString(any()) } returns "hello"

        val nameOfTestUser = GeneratedConfig(source).nameOfTestUser
        nameOfTestUser shouldBe "hello"

        verifyAll { source.getString("nameOfTestUser") }
    }

    @Test
    fun missingRequiredConfigTest() {
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
        val sourceText = readGeneratedConfigSource()

        sourceText shouldContain "* This is the basic configuration file."
    }

    @Test
    fun descriptionIsAttachedToProperty() {
        val sourceText = readGeneratedConfigSource()

        // Well, we're at least checking that the file contains this comment somewhere.
        sourceText shouldContain "* Maximum number of login tries before locking account."
    }

    @Test
    fun hasGeneratedAnnotation() {
        val sourceText = readGeneratedConfigSource()

        // Unfortunately Generated isn't retained at runtime, so we can't programmatically access it
        sourceText shouldContain "@Generated"
    }

    private fun readGeneratedConfigSource() =
        File(
            "../codegen-test-config/build/generated-sources-test/" +
                    "com/github/nanodeath/typedconfig/test/GeneratedConfig.kt"
        )
            .readText()

    @Test
    fun optionalTypesWork() {
        every { source.getInt(any()) } returns null
        every { source.getDouble(any()) } returns null
        every { source.getString(any()) } returns null

        val config = GeneratedConfig(source)
        config.optionalInt should beNull()
        config.optionalDouble should beNull()
        config.optionalStr should beNull()

        verifyAll {
            source.getInt("optionalInt")
            source.getDouble("optionalDouble")
            source.getString("optionalStr")
        }
    }
}
