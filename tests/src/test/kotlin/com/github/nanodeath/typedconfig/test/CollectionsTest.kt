package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
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

        verifyAll { source.getList("adminUsers") }
    }

    @Test
    fun requiredIntList() {
        every { source.getList(any()) } returns listOf("80", "8080")

        CollectionsConfig(source).allowedPorts shouldBe listOf(80, 8080)

        verifyAll { source.getList("allowedPorts") }
    }
}
