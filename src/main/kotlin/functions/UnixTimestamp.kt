package io.github.matoklimm.functions

import io.github.matoklimm.api.TemplateFunction
import io.github.matoklimm.core.ExecutionContext
import java.time.OffsetDateTime

internal class UnixTimestamp : TemplateFunction {
    override val name = "unixTimestamp"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        if (args.size > 1) {
            throw IllegalArgumentException("unixTimestamp() expects 0 or 1 argument but got ${args.size}")
        }

        val now = OffsetDateTime.now(ctx.clock).toInstant()
        return when {
            args.isEmpty() || args[0].equals("seconds", ignoreCase = true) ->
                now.epochSecond.toString()
            args[0].equals("millis", ignoreCase = true) ->
                now.toEpochMilli().toString()
            else -> throw IllegalArgumentException("unixTimestamp() expects 'seconds' oder 'millis' as argument but got '${args[0]}'")
        }
    }
}