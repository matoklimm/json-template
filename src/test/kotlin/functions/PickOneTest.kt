package functions

import io.github.matoklimm.core.ExecutionContext
import io.github.matoklimm.functions.PickOne
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

class PickOneTest {


    private lateinit var ctx: ExecutionContext
    private lateinit var fn: PickOne

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(123))
        fn = PickOne()
    }

    @Test
    fun `returning a random result`() {
        val states = mutableListOf("ON", "OFF", "ERROR")
        repeat(100) {
            val result = fn.execute(states, ctx)
            assertContains(states, result, "result should be one of $states but was $result")
        }
    }

    @Test
    fun `passing no args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf(), ctx)
        }
    }
}