package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.Test

class CollectionsTest {
    @Test
    fun requiredStringList() {
        val source = mockk<Source>()
        every { source.getList(any()) } returns listOf("root", "me")

        CollectionsConfig(source).adminUsers shouldBe listOf("root", "me")

        verifyAll { source.getList("adminUsers") }
    }

    @Test
    fun requiredIntList() {
        val source = mockk<Source>()
        every { source.getList(any()) } returns listOf("80", "8080")

        CollectionsConfig(source).allowedPorts shouldBe listOf(80, 8080)

        verifyAll { source.getList("allowedPorts") }
    }
}
