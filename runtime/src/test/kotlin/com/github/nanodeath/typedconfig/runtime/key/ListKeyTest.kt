package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class ListKeyTest {
    @Test
    fun canParseListOfInts() {
        val source = mockk<Source>()
        every { source.getList(any()) } returns listOf("1", "2", "3")

        val listKey = ListKey("listKey", source, null, emptyList()) { IntKey.parse(it) }

        listKey.resolve() shouldBe listOf(1, 2, 3)
    }

    @Test
    fun failsIfCantParse() {
        val source = mockk<Source>()
        every { source.getList(any()) } returns listOf("1", "2.0")

        val listKey = ListKey("listKey", source, null, emptyList()) { IntKey.parse(it) }

        shouldThrow<ParseException> {
            listKey.resolve()
        } should { e ->
            e.shouldHaveMessage("Failed to parse `listKey[1]`: failed to parse '2.0'")
            e.shouldHaveCauseInstanceOf<NumberFormatException>()
        }
    }
}
