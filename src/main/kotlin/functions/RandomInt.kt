package io.github.matoklimm.functions

import io.github.matoklimm.api.TemplateFunction
import io.github.matoklimm.core.ExecutionContext

internal class RandomInt : TemplateFunction {
    override val name = "randomInt"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        when (args.size) {
            0 -> return ctx.random.nextInt().toString()
            1 -> args.add("0")
            2 -> {}

            else -> throw IllegalArgumentException(
                "randomInt expects 0, 1 or 2 arguments but got ${args.size}"
            )
        }

        return getRandomValueFromMinMax(args, ctx)
    }

    private fun getRandomValueFromMinMax(args: List<String>, ctx: ExecutionContext): String {
        val firstArg = args[0].toIntOrNull()
            ?: throw IllegalArgumentException("randomInt(min,max): min must be an integer, got '${args[0]}'")
        val secondArg = args[1].toIntOrNull()
            ?: throw IllegalArgumentException("randomInt(min,max): max must be an integer, got '${args[1]}'")

        val min = minOf(firstArg, secondArg)
        var max = maxOf(firstArg, secondArg)

        if (max > 0) max += 1

        return ctx.random.nextInt(min, max).toString()
    }
}