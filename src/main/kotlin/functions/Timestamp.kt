package io.github.klimmmax.functions

import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.ExecutionContext
import java.time.OffsetDateTime

class Timestamp : TemplateFunction {
    override val name = "timestamp"

    override fun execute(args: List<String>, ctx: ExecutionContext): String {
        return when (args.size) {
            0 -> OffsetDateTime.now(ctx.clock).toString()
            else -> throw IllegalArgumentException("timestamp will always return 'now()', if you want to use interval borders, use 'randomTimestamp()' instead")
        }
    }
}