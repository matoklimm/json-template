package io.github.klimmmax.functions

import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.ExecutionContext
import java.util.UUID

internal class RandomUuid: TemplateFunction {
    override val name = "randomUuid"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext) = UUID.randomUUID().toString()
}