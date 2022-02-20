package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.ParseException
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveCauseInstanceOf
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ListKeyTest {
    @MockK
    lateinit var source: Source

    @Test
    fun canParseListOfInts() {
        every { source.getList(any()) } returns listOf("1", "2", "3")

        val listKey = ListKey("listKey", source, null, emptyList()) { IntKey.parse(it) }

        listKey.resolve() shouldBe listOf(1, 2, 3)
    }

    @Test
    fun failsIfCantParse() {
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
