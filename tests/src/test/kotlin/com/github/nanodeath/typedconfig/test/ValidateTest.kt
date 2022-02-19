package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

class ValidateTest {
    @Test
    fun checksEachKey() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 80

        RequiredKeyConfig(source).validate()

        verifyAll {
            source.getInt("port")
            source.getInt("database.port")
        }
    }

    @Test
    fun failsIfRequiredTopLevelKeyAbsent() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 80
        every { source.getInt("port") } returns null

        shouldThrow<MissingConfigurationException> {
            RequiredKeyConfig(source).validate()
        }.should { ex ->
            ex.shouldHaveMessage("Config `port` is required but could not be resolved and no default was provided")
        }
    }

    @Test
    fun failsIfRequiredNestedKeyAbsent() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 80
        every { source.getInt("database.port") } returns null

        shouldThrow<MissingConfigurationException> {
            RequiredKeyConfig(source).validate()
        }.should { ex ->
            ex.shouldHaveMessage("Config `database.port` is required but could not be resolved and no default was provided")
        }
    }
}
