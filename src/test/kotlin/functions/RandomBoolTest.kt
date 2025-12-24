package functions

import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.functions.RandomBool
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertContains
import kotlin.test.assertFalse

class RandomBoolTest {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: RandomBool

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(123))
        fn = RandomBool()
    }

    @Test
    fun `always returning a random bool`() {
        val result = fn.execute(emptyList(), ctx)
        repeat(100) {
            assertContains(listOf("true", "false"), result, "result should be either 'true' or 'false' but was $result")
        }
    }

    @Test
    fun `arguments will be ignored during execute`() {
        val result = fn.execute(listOf("testString", "123"), ctx)
        assertFalse(result.toBoolean())
    }

}