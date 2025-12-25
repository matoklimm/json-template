package io.github.klimmmax.functions

import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.ExecutionContext
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

class RandomTimestamp : TemplateFunction {
    override val name = "randomTimestamp"

    override fun execute(args: List<String>, ctx: ExecutionContext): String {
        if (args.size == 1) {
            val border = try {
                OffsetDateTime.parse(args[0])
            } catch (_: DateTimeParseException) {
                throw IllegalArgumentException(
                    "randomTimestamp(border): invalid ISO-8601 timestamp, got '${args[0]}'"
                )
            }
            val now = OffsetDateTime.now(ctx.clock)
            val start = minOf(now,border)
            val end = maxOf(now, border)
            return getRandomTimestampBetween(start, end, ctx)
        }

        if (args.size == 2) {
            val firstBorder = try {
                OffsetDateTime.parse(args[0])
            } catch (_: DateTimeParseException) {
                throw IllegalArgumentException(
                    "randomTimestamp(border): invalid ISO-8601 timestamp, got '${args[0]}'"
                )
            }

            val secondBorder = try {
                OffsetDateTime.parse(args[1])
            } catch (_: DateTimeParseException) {
                throw IllegalArgumentException(
                    "randomTimestamp(border): invalid ISO-8601 timestamp, got '${args[1]}'"
                )
            }

            val start = minOf(firstBorder,secondBorder)
            val end = maxOf(firstBorder, secondBorder)
            return getRandomTimestampBetween(start, end, ctx)
        }

        throw IllegalArgumentException("randomTimestamp() expects either 1 or 2 arguments but got $args")
    }

    private fun getRandomTimestampBetween(start: OffsetDateTime, end: OffsetDateTime, ctx: ExecutionContext): String {
        val diffMillis = Duration.between(start.toInstant(), end.toInstant()).toMillis()
        val randomOffset = ctx.random.nextLong(diffMillis + 1)
        val randomInstant = start.toInstant().plusMillis(randomOffset)
        return OffsetDateTime.ofInstant(randomInstant, ZoneOffset.UTC).toString()
    }

}