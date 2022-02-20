package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.MissingConfigurationException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ValidateTest {
    @MockK
    lateinit var source: Source

    @Test
    fun checksEachKey() {
        every { source.getInt(any()) } returns 80

        RequiredKeyConfig(source).validate()

        verifyAll {
            source.getInt("port")
            source.getInt("database.port")
        }
    }

    @Test
    fun failsIfRequiredTopLevelKeyAbsent() {
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
        every { source.getInt(any()) } returns 80
        every { source.getInt("database.port") } returns null

        shouldThrow<MissingConfigurationException> {
            RequiredKeyConfig(source).validate()
        }.should { ex ->
            ex.shouldHaveMessage(
                "Config `database.port` is required but could not be resolved and no default was provided"
            )
        }
    }
}
