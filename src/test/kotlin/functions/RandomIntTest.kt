package functions

import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.functions.RandomInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RandomIntTest {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: RandomInt

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(123))
        fn = RandomInt()
    }

    @Test
    fun `no args returns any integer`() {
        val result = fn.execute(emptyList(), ctx).toInt()
        // deterministic because of seed
        assertEquals(296510812, result)
    }

    @Test
    fun `one arg uses upper bound`() {
        val result = fn.execute(listOf("10"), ctx).toInt()
        assertTrue(result in 0..10)
    }

    @Test
    fun `one arg uses lower bound if arg is negative`() {
        val result = fn.execute(listOf("-10"), ctx).toInt()
        assertTrue(result in -10..0)
    }

    @Test
    fun `two args use min and max`() {
        repeat(100) {
            val value = fn.execute(listOf("1", "7"), ctx).toInt()
            assertTrue(value in 1..7)
        }
    }

    @Test
    fun `min greater than max throws`() {
        val ex = assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("10", "5"), ctx)
        }
        assertTrue(ex.message!!.contains("min (10) must be <= max (5)"))
    }

    @Test
    fun `non integer arg throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("abc"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("2147483648"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("-2147483649", "0"), ctx)
        }
    }

    @Test
    fun `too many args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("1", "2", "3"), ctx)
        }
    }
}