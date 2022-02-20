package com.github.nanodeath.typedconfig.test

import com.github.nanodeath.typedconfig.runtime.TypedConfig
import com.github.nanodeath.typedconfig.runtime.source.EnvSource
import com.github.nanodeath.typedconfig.runtime.source.Source
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verifyAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

@ExtendWith(MockKExtension::class)
class DefaultConfigTest {
    @MockK
    lateinit var source: Source

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
        every { source.getString(any()) } returns "hello"
        TypedConfig.defaultSource = source

        val nameOfTestUser = GeneratedConfig.default().nameOfTestUser
        nameOfTestUser shouldBe "hello"

        verifyAll { source.getString("nameOfTestUser") }
    }

    @Test
    fun canOnlySetOnce() {
        val source1 = mockk<Source>("Source1")
        val source2 = mockk<Source>("Source2")
        TypedConfig.defaultSource = source1
        shouldThrowUnit<IllegalStateException> {
            TypedConfig.defaultSource = source2
        }
    }
}
