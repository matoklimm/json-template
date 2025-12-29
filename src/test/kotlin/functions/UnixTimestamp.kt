package functions

import io.github.matoklimm.core.ExecutionContext
import io.github.matoklimm.functions.UnixTimestamp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlin.random.Random
import kotlin.test.assertFailsWith

class UnixTimestamp {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: UnixTimestamp

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(123), Clock.fixed(Instant.parse("2025-12-24T13:37:00Z"), ZoneOffset.UTC))
        fn = UnixTimestamp()
    }

    @Test
    fun `returns unix timestamp in seconds by default`() {
        val result = fn.execute(mutableListOf(), ctx)
        assertEquals("1766564700", result)
    }

    @Test
    fun `returns unix timestamp in seconds when argument is seconds`() {
        val result = fn.execute(mutableListOf("seconds"), ctx)
        assertEquals("1766564700", result)
    }

    @Test
    fun `returns unix timestamp in millis when argument is millis`() {
        val result = fn.execute(mutableListOf("millis"), ctx)
        assertEquals("1766564700000", result)
    }

    @Test
    fun `throws on invalid argument(s)`() {
        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("foo"), ctx)
        }

        assertFailsWith<IllegalArgumentException> {
            fn.execute(mutableListOf("seconds", "bar"), ctx)
        }
    }
}