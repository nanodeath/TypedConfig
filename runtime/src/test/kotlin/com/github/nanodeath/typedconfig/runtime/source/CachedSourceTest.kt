package com.github.nanodeath.typedconfig.runtime.source

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CachedSourceTest {
    @MockK
    lateinit var innerSource: Source

    private val subject by lazy { innerSource.cached() }

    @Test
    fun cachesInts() {
        every { innerSource.getInt(any()) } returns 1

        subject.getInt("foo") shouldBe 1
        subject.getInt("foo") shouldBe 1

        verify(atMost = 1) { innerSource.getInt("foo") }
    }

    @Test
    fun cachesStrings() {
        every { innerSource.getString(any()) } returns "bar"

        subject.getString("foo") shouldBe "bar"
        subject.getString("foo") shouldBe "bar"

        verify(atMost = 1) { innerSource.getString("foo") }
    }

    @Test
    fun cachesDoubles() {
        every { innerSource.getDouble(any()) } returns 3.14

        subject.getDouble("foo") shouldBe 3.14
        subject.getDouble("foo") shouldBe 3.14

        verify(atMost = 1) { innerSource.getDouble("foo") }
    }

    @Test
    fun cachesBooleans() {
        every { innerSource.getBoolean(any()) } returns true

        subject.getBoolean("foo") shouldBe true
        subject.getBoolean("foo") shouldBe true

        verify(atMost = 1) { innerSource.getBoolean("foo") }
    }

    @Test
    fun cachesLists() {
        every { innerSource.getList(any()) } returns listOf("1", "2")

        subject.getList("foo") shouldBe listOf("1", "2")
        subject.getList("foo") shouldBe listOf("1", "2")

        verify(atMost = 1) { innerSource.getList("foo") }
    }
}
