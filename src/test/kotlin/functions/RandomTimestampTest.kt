package functions

import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.functions.RandomTimestamp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.test.assertFailsWith

class RandomTimestampTest {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: RandomTimestamp

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(123), Clock.fixed(Instant.parse("2025-12-24T13:45:00Z"), ZoneOffset.UTC))
        fn = RandomTimestamp()
    }

    @Test
    fun `one timestamp will be used as later bound for random timestamp if timestamp is later then now()`() {
        val result = fn.execute(listOf("2025-12-25T13:37:00Z"), ctx)
        assertEquals("2025-12-24T14:29:08.233Z", result)
    }

    @Test
    fun `using different timezone for one timestamp will be parsed correctly()`() {
        val result = fn.execute(listOf("2025-12-25T15:37:00+02"), ctx)
        assertEquals("2025-12-24T14:29:08.233Z", result)
    }

    @Test
    fun `one timestamp will be used as earlier bound for random timestamp if timestamp is earlier then now()`() {
        val result = fn.execute(listOf("2025-12-23T01:42:00Z"), ctx)
        assertEquals("2025-12-24T09:46:20.188Z", result)
    }

    @Test
    fun `two timestamps will be used to determine a random timestamp in between`() {
        val result = fn.execute(listOf("2025-12-23T01:42:00Z", "2025-12-25T13:37:00Z"), ctx)
        assertEquals("2025-12-25T02:23:10.012Z", result)
    }

    @Test
    fun `ordering of two timestamps does not matter`() {
        val result = fn.execute(listOf("2025-12-25T13:37:00Z", "2025-12-23T01:42:00Z"), ctx)
        assertEquals("2025-12-25T02:23:10.012Z", result)
    }

    @Test
    fun `non timestamp strings arg throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("abc"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("256"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("2025-12-25"), ctx)
        }
    }

    @Test
    fun `no args or too many args throws`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf("1", "2", "3"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(listOf(), ctx)
        }
    }

}