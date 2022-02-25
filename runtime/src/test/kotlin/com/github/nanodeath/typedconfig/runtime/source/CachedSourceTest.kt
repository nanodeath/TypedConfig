package com.github.nanodeath.typedconfig.runtime.source

import com.github.nanodeath.typedconfig.runtime.key.SimpleKey
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

        subject.getInt(SimpleKey("foo")) shouldBe 1
        subject.getInt(SimpleKey("foo")) shouldBe 1

        verify(atMost = 1) { innerSource.getInt(SimpleKey("foo")) }
    }

    @Test
    fun cachesStrings() {
        every { innerSource.getString(any()) } returns "bar"

        subject.getString(SimpleKey("foo")) shouldBe "bar"
        subject.getString(SimpleKey("foo")) shouldBe "bar"

        verify(atMost = 1) { innerSource.getString(SimpleKey("foo")) }
    }

    @Test
    fun cachesDoubles() {
        every { innerSource.getDouble(any()) } returns 3.14

        subject.getDouble(SimpleKey("foo")) shouldBe 3.14
        subject.getDouble(SimpleKey("foo")) shouldBe 3.14

        verify(atMost = 1) { innerSource.getDouble(SimpleKey("foo")) }
    }

    @Test
    fun cachesBooleans() {
        every { innerSource.getBoolean(any()) } returns true

        subject.getBoolean(SimpleKey("foo")) shouldBe true
        subject.getBoolean(SimpleKey("foo")) shouldBe true

        verify(atMost = 1) { innerSource.getBoolean(SimpleKey("foo")) }
    }

    @Test
    fun cachesLists() {
        every { innerSource.getList(any()) } returns listOf("1", "2")

        subject.getList(SimpleKey("foo")) shouldBe listOf("1", "2")
        subject.getList(SimpleKey("foo")) shouldBe listOf("1", "2")

        verify(atMost = 1) { innerSource.getList(SimpleKey("foo")) }
    }
}
