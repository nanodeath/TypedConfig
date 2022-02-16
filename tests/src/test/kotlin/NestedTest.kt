import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verifyAll
import name.maxaller.konfigurator.runtime.source.Source
import name.maxaller.konfigurator.test.NestedConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class NestedTest {
    @MockK
    lateinit var source: Source

    private val subject by lazy { NestedConfig(source) }

    @Test
    fun sanity() {
        every { source.getInt(any()) } returns 500

        subject.app.port shouldBe 500

        verifyAll { source.getInt("app.port") }
    }
}
