package com.github.nanodeath.typedconfig.koin

import com.github.nanodeath.typedconfig.runtime.source.MapSource
import com.github.nanodeath.typedconfig.test.KoinConfig
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.optional.shouldNotBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import java.util.*

class KoinModuleFactoryTest : KoinTest {
    private val key by inject<Int>(named("integer"))
    private val nestedKey by inject<String>(named("nested.foo"))
    private val optionalKey by inject<Optional<Int>>(named("optionalKey"))
    private val optionalKey2 by inject<Optional<Int>>(named("optionalKey2"))

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        val input = mapOf(
            "integer" to 42,
            "nested.foo" to "bar",
            "optionalKey" to 1,
            // "optionalKey2" intentionally omitted
        )
        modules(KoinConfig(MapSource(input)).asKoinModule())
    }

    @Test
    fun topLevelKey() {
        key shouldBe 42
    }

    @Test
    fun nestedKey() {
        nestedKey shouldBe "bar"
    }

    @Test
    fun optionalAndPresent() {
        optionalKey shouldBePresent {
            it shouldBe 1
        }
    }

    @Test
    fun optionalButAbsent() {
        optionalKey2.shouldNotBePresent()
    }
}
