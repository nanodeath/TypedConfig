package com.github.nanodeath.typedconfig.test

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import com.github.nanodeath.typedconfig.runtime.source.Source
import org.junit.jupiter.api.Test

class BasicTest {
    @Test
    fun sanity() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 5

        val loginTries = GeneratedConfig(source).maxLoginTries
        loginTries shouldBe 5

        verifyAll { source.getInt("maxLoginTries") }
    }

    @Test
    fun stringKeyTest() {
        val source = mockk<Source>()
        every { source.getString(any()) } returns "hello"

        val nameOfTestUser = GeneratedConfig(source).nameOfTestUser
        nameOfTestUser shouldBe "hello"

        verifyAll { source.getString("nameOfTestUser") }
    }
}