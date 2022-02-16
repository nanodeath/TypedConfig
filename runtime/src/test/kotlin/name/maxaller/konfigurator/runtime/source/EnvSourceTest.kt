package name.maxaller.konfigurator.runtime.source

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class EnvSourceTest {
    @MockK
    lateinit var env: Env

    private val subject by lazy { EnvSource(env) }

    @Test
    fun getStringTest() {
        every { env.get("FOO") } returns "bar"

        subject.getString("foo") shouldBe "bar"

        verifyAll { env.get("FOO") }
    }

    @Test
    fun getStringCaseTest() {
        every { env.get("FOO_BAR_BAZ") } returns "bar"

        subject.getString("fooBarBaz") shouldBe "bar"

        verifyAll { env.get("FOO_BAR_BAZ") }
    }
}
