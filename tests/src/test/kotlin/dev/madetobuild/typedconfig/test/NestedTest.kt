package dev.madetobuild.typedconfig.test

import dev.madetobuild.typedconfig.runtime.key.keyWithName
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import dev.madetobuild.typedconfig.runtime.source.Source
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NestedTest {
    @MockK
    lateinit var source: Source

    private val subject by lazy { NestedConfig(source) }

    @Test
    fun sanity() {
        every { source.getInt(any()) } returns 500

        subject.app.port shouldBe 500

        verifyAll { source.getInt(keyWithName("app.port")) }
    }

    @Test
    fun veryNested() {
        every { source.getString(any()) } returns "user"

        subject.database.credentials.username shouldBe "user"

        verifyAll { source.getString(keyWithName("database.credentials.username")) }
    }
}
