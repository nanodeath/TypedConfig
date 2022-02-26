package dev.madetobuild.typedconfig.test

import dev.madetobuild.typedconfig.runtime.Key
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.junit.jupiter.api.Test
import kotlin.reflect.full.findAnnotation

class AnnotationsTest {
    @Test
    fun topLevelKeys() {
        GeneratedConfig::maxLoginTries.annotations shouldNot beEmpty()
        val annotation = GeneratedConfig::maxLoginTries.findAnnotation<Key>()
        annotation shouldNot beNull()
        annotation?.name shouldBe "maxLoginTries"
    }

    @Test
    fun nestedKeys() {
        val annotation = NestedConfig.Database::port.findAnnotation<Key>()
        annotation shouldNot beNull()
        annotation?.name shouldBe "database.port"
    }
}
