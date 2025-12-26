package io.github.klimmmax.functions

import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.ExecutionContext

internal class RandomString : TemplateFunction {
    override val name = "randomString"

    companion object {
        private const val MAX_LENGTH = 255
    }

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        return when (args.size) {
            0 -> getRandomString(12, ctx)

            1 -> {
                val max = args[0].toIntOrNull()
                    ?: throw IllegalArgumentException("randomString(max): max must be an integer, got '${args[0]}'")
                require(max in 0..MAX_LENGTH) {
                    throw IllegalArgumentException("randomString(max): max length must be  between 0 and 255, got '${args[0]}'")
                }

                getRandomString(max, ctx)
            }

            2 -> {
                val max = args[0].toIntOrNull()
                    ?: throw IllegalArgumentException("randomString(max, prefix): max must be an integer, got '${args[0]}'")
                require(max in 0..MAX_LENGTH) {
                    throw IllegalArgumentException("randomString(max, prefix): max length must be  between 0 and 255, got '${args[0]}'")
                }

                args[1] + getRandomString(max, ctx)
            }

            else -> throw IllegalArgumentException(
                "randomString expects 0, 1 or 2 arguments but got ${args.size}"
            )
        }
    }

    private fun getRandomString(length: Int, ctx: ExecutionContext): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return buildString(length) {
            repeat(length) {
                append(allowedChars.random(ctx.random))
            }
        }
    }
}