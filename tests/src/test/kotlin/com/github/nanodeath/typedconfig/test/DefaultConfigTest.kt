package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.TypedConfig
import com.github.nanodeath.typedconfig.runtime.source.EnvSource
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class DefaultConfigTest {
    companion object {
        private lateinit var typedConfigInitialized: KMutableProperty<Boolean>

        @BeforeAll
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun getInitializationProperty() {
            typedConfigInitialized =
                TypedConfig::class.declaredMemberProperties
                    .single { it.name == "sourceInitialized" } as KMutableProperty<Boolean>
            typedConfigInitialized.setter.isAccessible = true
        }

        fun markInitialized(initialized: Boolean) {
            typedConfigInitialized.setter.call(initialized)
        }
    }

    @BeforeEach
    fun setup() {
        markInitialized(false)
    }

    @AfterEach
    fun teardown() {
        markInitialized(false)
        TypedConfig.defaultSource = EnvSource()
        markInitialized(false)
    }

    @Test
    fun works() {
        val source = mockk<Source>()
        every { source.getString(any()) } returns "hello"
        TypedConfig.defaultSource = source

        val nameOfTestUser = GeneratedConfig.default().nameOfTestUser
        nameOfTestUser shouldBe "hello"

        verifyAll { source.getString("nameOfTestUser") }
    }
}
