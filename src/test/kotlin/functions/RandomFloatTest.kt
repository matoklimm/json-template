package functions

import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.functions.RandomFloat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random

class RandomFloatTest {

    private lateinit var ctx: ExecutionContext
    private lateinit var fn: RandomFloat

    @BeforeEach
    fun setup() {
        ctx = ExecutionContext(random = Random(541))
        fn = RandomFloat()
    }

    @Test
    fun `no args returns any float between 0 and 1 rounded to 2 decimals`() {
        val result = fn.execute(mutableListOf(), ctx)
        assertEquals("0.83", result)
    }

    @Test
    fun `one arg returns any float between 0 and the passed value rounded to 2 decimals`() {
        val positiveResult = fn.execute(mutableListOf("95"), ctx)
        val negativeResult = fn.execute(mutableListOf("-13"), ctx)
        assertEquals("78.49", positiveResult)
        assertEquals("-12.43", negativeResult)
    }

    @Test
    fun `two args returns any float between min,max of the passed values rounded to 2 decimals`() {
        val positiveResult = fn.execute(mutableListOf("13", "37"), ctx)
        val negativeResult = fn.execute(mutableListOf("-2", "-11"), ctx)
        assertEquals("32.83", positiveResult)
        assertEquals("-10.61", negativeResult)
    }

    @Test
    fun `three args are used as min, max and number of decimals to round`() {
        val positiveResult = fn.execute(mutableListOf("13", "37", "4"), ctx)
        val negativeResult = fn.execute(mutableListOf("-2", "-11", "1"), ctx)
        assertEquals("32.8295", positiveResult)
        assertEquals("-10.6", negativeResult)
    }
}