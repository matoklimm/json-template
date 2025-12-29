package functions

import io.github.matoklimm.core.ExecutionContext
import io.github.matoklimm.functions.RandomInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random
import org.junit.jupiter.api.Test
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
        val result = fn.execute(mutableListOf(), ctx).toInt()
        // deterministic because of seed
        assertEquals(296510812, result)
    }

    @Test
    fun `one arg uses upper bound`() {
        val result = fn.execute(mutableListOf("10"), ctx).toInt()
        assertTrue(result in 0..10)
    }

    @Test
    fun `one arg uses lower bound if arg is negative`() {
        val result = fn.execute(mutableListOf("-10"), ctx).toInt()
        assertTrue(result in -10..0)
    }

    @Test
    fun `two args use min and max`() {
        repeat(100) {
            val value = fn.execute(mutableListOf("1", "7"), ctx).toInt()
            assertTrue(value in 1..7)
        }
    }

    @Test
    fun `min greater than max still returns a valid result`() {
        val value = fn.execute(mutableListOf("10", "5"), ctx).toInt()
        assertTrue(value in 5..10)
    }

    @Test
    fun `passing 0 as min and max throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("0", "0"), ctx)
        }
    }

    @Test
    fun `non integer arg throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("abc"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("2147483648"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("-2147483649", "0"), ctx)
        }
    }

    @Test
    fun `too many args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("1", "2", "3"), ctx)
        }
    }
}