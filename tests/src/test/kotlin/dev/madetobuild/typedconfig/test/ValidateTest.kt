package dev.madetobuild.typedconfig.test

import dev.madetobuild.typedconfig.runtime.MissingConfigurationException
import dev.madetobuild.typedconfig.runtime.key.keyWithName
import dev.madetobuild.typedconfig.runtime.source.Source
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
            source.getInt(keyWithName("port"))
            source.getInt(keyWithName("database.port"))
        }
    }

    @Test
    fun failsIfRequiredTopLevelKeyAbsent() {
        every { source.getInt(any()) } returns 80
        every { source.getInt(keyWithName("port")) } returns null

        shouldThrow<MissingConfigurationException> {
            RequiredKeyConfig(source).validate()
        }.should { ex ->
            ex.shouldHaveMessage("Config `port` is required but could not be resolved and no default was provided")
        }
    }

    @Test
    fun failsIfRequiredNestedKeyAbsent() {
        every { source.getInt(any()) } returns 80
        every { source.getInt(keyWithName("database.port")) } returns null

        shouldThrow<MissingConfigurationException> {
            RequiredKeyConfig(source).validate()
        }.should { ex ->
            ex.shouldHaveMessage(
                "Config `database.port` is required but could not be resolved and no default was provided"
            )
        }
    }
}
