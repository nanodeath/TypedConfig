package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.codegen.ConfigSpecReader
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.should
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.Test

class InvalidSensitiveKeyTest {
    @Test
    fun complainsAboutUnsupportedAttribute() {
        shouldThrow<UnsupportedOperationException> {
            ConfigSpecReader()
                .translateIntoCode(javaClass.getResourceAsStream("/invalid_sensitive_key/config.tc.toml")!!)
        } should { ex ->
            ex.shouldHaveMessage(
                "The `int` type (for key 'intCantBeSensitive') doesn't support the `sensitive` attribute")
        }
    }
}
