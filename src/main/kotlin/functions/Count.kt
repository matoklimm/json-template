package io.github.klimmmax.functions

import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.ExecutionContext

class Count : TemplateFunction {
    override val name = "count"

    override fun execute(args: List<String>, ctx: ExecutionContext): String {
        var currentValue = (ctx.state[name] ?: 0L) as Long
        var step = 1

        when (args.size) {
            0 -> {}
            1 -> { step = args[0].toIntOrNull() ?: throw IllegalArgumentException("count(step): step must be an integer, got '${args[0]}'") }
            2 -> {
                val startIndex = args[0].toLongOrNull()
                    ?: throw IllegalArgumentException("count(start, step): start must be a long, got '${args[0]}'")
                step = args[1].toIntOrNull()
                    ?: throw IllegalArgumentException("count(start, step): step must be an integer, got '${args[1]}'")

                if (currentValue == 0L) {
                    ctx.state[name] = startIndex
                    return startIndex.toString()
                }
            }

            else -> throw IllegalArgumentException("count() expects 0, 1 or 2 arguments but got ${args.size}")
        }

        currentValue += step
        ctx.state[name] = currentValue
        return currentValue.toString()
    }
}