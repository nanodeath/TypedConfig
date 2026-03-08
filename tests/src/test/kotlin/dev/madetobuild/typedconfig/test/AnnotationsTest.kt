package dev.madetobuild.typedconfig.test

import dev.madetobuild.typedconfig.runtime.ConfigMetadata
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
        annotation?.description shouldBe "Maximum number of login tries before locking account."
        annotation?.type shouldBe "int"
        annotation?.required shouldBe true
        annotation?.sensitive shouldBe false
    }

    @Test
    fun nestedKeys() {
        val annotation = NestedConfig.Database::port.findAnnotation<Key>()
        annotation shouldNot beNull()
        annotation?.name shouldBe "database.port"
        annotation?.type shouldBe "int"
        annotation?.required shouldBe true
    }

    @Test
    fun configMetadata() {
        val metadata = GeneratedConfig::class.findAnnotation<ConfigMetadata>()
        metadata shouldNot beNull()
        metadata?.description shouldBe "This is the basic configuration file."
        metadata?.namespace shouldBe ""
        metadata?.sourceFile?.contains("basic_test/config.tc.toml") shouldBe true
    }

    @Test
    fun configMetadataNamespaced() {
        val metadata = NamespacedConfig::class.findAnnotation<ConfigMetadata>()
        metadata shouldNot beNull()
        metadata?.namespace shouldBe "app"
    }
}
