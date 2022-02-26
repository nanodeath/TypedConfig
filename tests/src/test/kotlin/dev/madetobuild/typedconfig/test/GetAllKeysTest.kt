package dev.madetobuild.typedconfig.test

import dev.madetobuild.typedconfig.runtime.key.Key
import io.kotest.assertions.forEachAsClue
import io.kotest.matchers.collections.atLeastSize
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.reflect.KProperty0

class GetAllKeysTest {
    @Test
    fun topLevelKeys() {
        val keys = GeneratedConfig.default().getAllKeys().toList()
        keys shouldBe atLeastSize<Any>(5)
        keys.forEachAsClue { prop -> isKey(prop) }
    }

    @Test
    fun nestedKeys() {
        val keys = NestedConfig.default().getAllKeys().toList()
        keys shouldBe atLeastSize<Any>(2)
        keys.forEachAsClue { prop -> isKey(prop) }
    }

    private fun isKey(prop: KProperty0<*>) {
        prop.annotations.shouldExist { it.annotationClass.simpleName == Key::class.simpleName }
    }
}
