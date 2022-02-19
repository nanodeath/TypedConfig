package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class NamespacedConfigTest {
    @Test
    fun namespacedKey() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 8080

        NamespacedConfig(source).app.port shouldBe 8080

        verify { source.getInt("app.port") }
    }

    @Test
    fun namespacedCompoundKey() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 5432

        NamespacedConfig(source).app.databasePort shouldBe 5432

        verify { source.getInt("app.databasePort") }
    }

    @Test
    fun namespacedNestedKey() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 42

        NamespacedConfig(source).app.nested.key shouldBe 42

        verify { source.getInt("app.nested.key") }
    }
}
