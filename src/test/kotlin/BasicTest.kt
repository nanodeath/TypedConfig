import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import name.maxaller.konfigurator.runtime.Source
import name.maxaller.konfigurator.test.GeneratedConfig
import org.junit.jupiter.api.Test

class BasicTest {
    @Test
    fun sanity() {
        val source = mockk<Source>()
        every { source.getInt(any()) } returns 5

        val loginTries = GeneratedConfig(source).maxLoginTries
        loginTries shouldBe 5

        verifyAll { source.getInt("maxLoginTries") }
    }
}
