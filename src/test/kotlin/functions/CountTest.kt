package functions

import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.functions.Count
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class CountTest {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: Count

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext()
        fn = Count()
    }

    @Test
    fun `no args is just counting up like a loop with steps of 1`() {
        for (i in 1..100) {
            val result = fn.execute(emptyList(), ctx).toInt()
            assertEquals(i, result)
        }
    }

    @Test
    fun `first argument is used as a step argument`() {
        for (i in 2..100 step 2) {
            val result = fn.execute(listOf("2"), ctx).toInt()
            assertEquals(i, result)
        }
    }

    @Test
    fun `first arg is used as start value and second arg is used as step`() {
        var result = fn.execute(listOf("10", "2"), ctx).toInt()
        assertEquals(10, result)    // first invocation, we use start index from arg
        for (i in 12..100 step 2) {
            result = fn.execute(listOf("10", "2"), ctx).toInt() // since the start index was already used, just increase by step 2
            assertEquals(i, result)
        }
    }

    @Test
    fun `first arg is close to LONG_MAX_VALUE will overflow`() {
        val result = fn.execute(listOf("9223372036854775806", "3"), ctx).toLong()
        assertEquals(9223372036854775806, result)
        val result2 = fn.execute(listOf("9223372036854775806", "3"), ctx).toLong()
        assertEquals(-9223372036854775807, result2)
    }

    @Test
    fun `too many args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("1", "2", "3"), ctx)
        }
    }

    @Test
    fun `passing too big args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("2147483648"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("9223372036854775808", "2"), ctx)
        }
    }
}