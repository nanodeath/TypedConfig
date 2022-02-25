package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.key.keyWithName
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NamespacedConfigTest {
    @MockK
    lateinit var source: Source

    @Test
    fun namespacedKey() {
        every { source.getInt(any()) } returns 8080

        NamespacedConfig(source).app.port shouldBe 8080

        verify { source.getInt(keyWithName("app.port")) }
    }

    @Test
    fun namespacedCompoundKey() {
        every { source.getInt(any()) } returns 5432

        NamespacedConfig(source).app.databasePort shouldBe 5432

        verify { source.getInt(keyWithName("app.databasePort")) }
    }

    @Test
    fun namespacedNestedKey() {
        every { source.getInt(any()) } returns 42

        NamespacedConfig(source).app.nested.key shouldBe 42

        verify { source.getInt(keyWithName("app.nested.key")) }
    }
}
