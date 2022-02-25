package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.key.keyWithName
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CollectionsTest {
    @MockK
    lateinit var source: Source

    @Test
    fun requiredStringList() {
        every { source.getList(any()) } returns listOf("root", "me")

        CollectionsConfig(source).adminUsers shouldBe listOf("root", "me")

        verifyAll { source.getList(keyWithName("adminUsers")) }
    }

    @Test
    fun requiredIntList() {
        every { source.getList(any()) } returns listOf("80", "8080")

        CollectionsConfig(source).allowedPorts shouldBe listOf(80, 8080)

        verifyAll { source.getList(keyWithName("allowedPorts")) }
    }

    @Test
    fun requiredIntSet() {
        every { source.getList(any()) } returns listOf("80", "8080")

        CollectionsConfig(source).uniqueIds shouldBe setOf(80, 8080)

        verifyAll { source.getList(keyWithName("uniqueIds")) }
    }

    @Test
    fun defaultIntSet() {
        every { source.getList(any()) } returns null andThen listOf("80", "8080")

        CollectionsConfig(source).uniqueIdsWithDefaults shouldBe setOf(1, 2, 3)
        CollectionsConfig(source).uniqueIdsWithDefaults shouldBe setOf(80, 8080)

        verify(exactly = 2) { source.getList(keyWithName("uniqueIdsWithDefaults")) }
    }

    @Test
    fun optionalIntSet() {
        every { source.getList(any()) } returns null andThen listOf("80", "8080")

        CollectionsConfig(source).optionalUniqueIds should beNull()
        CollectionsConfig(source).optionalUniqueIds shouldBe setOf(80, 8080)

        verify(exactly = 2) { source.getList(keyWithName("optionalUniqueIds")) }
    }
}
