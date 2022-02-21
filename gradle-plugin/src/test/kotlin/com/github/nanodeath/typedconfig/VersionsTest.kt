package com.github.nanodeath.typedconfig

import io.kotest.matchers.should
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VersionsTest {
    @Test
    fun codegenTest() {
        val versions = Versions(PluginProperties(mapOf("version" to "FOO")))
        versions.codegenDependency should {
            it shouldContain ":typedconfig-codegen:"
            it shouldEndWith "FOO"
        }
    }

    @Test
    fun runtimeTest() {
        val versions = Versions(PluginProperties(mapOf("version" to "FOO")))
        versions.runtimeDependency should {
            it shouldContain ":typedconfig-runtime:"
            it shouldEndWith "FOO"
        }
    }
}
