package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.TypedConfig
import com.github.nanodeath.typedconfig.runtime.source.EnvSource
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrow
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
        private lateinit var writeOnceDelegate: Any
        private lateinit var typedConfigInitialized: KMutableProperty<Boolean>

        @BeforeAll
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun getInitializationProperty() {
            val defaultSource = TypedConfig::class.declaredMemberProperties.single { it.name == "defaultSource" }
            defaultSource.isAccessible = true
            writeOnceDelegate = defaultSource.getDelegate(TypedConfig)!!
            typedConfigInitialized =
                writeOnceDelegate::class.declaredMemberProperties
                    .single { it.name == "initialized" } as KMutableProperty<Boolean>
            typedConfigInitialized.setter.isAccessible = true
        }

        fun setInitialized(initialized: Boolean) {
            typedConfigInitialized.setter.call(writeOnceDelegate, initialized)
        }
    }

    @BeforeEach
    fun setup() {
        setInitialized(false)
    }

    @AfterEach
    fun teardown() {
        setInitialized(false)
        TypedConfig.defaultSource = EnvSource()
        setInitialized(false)
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

    @Suppress("RedundantUnitExpression")
    @Test
    fun canOnlySetOnce() {
        val source = mockk<Source>("Source1")
        val source2 = mockk<Source>("Source2")
        TypedConfig.defaultSource = source
        shouldThrow<IllegalStateException> {
            TypedConfig.defaultSource = source2
            Unit
        }
    }
}
