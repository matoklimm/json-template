package io.github.matoklimm.functions

import io.github.matoklimm.api.TemplateFunction
import io.github.matoklimm.core.ExecutionContext

internal class PickOne: TemplateFunction {
    override val name = "pickOne"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        require(args.isNotEmpty()) { throw IllegalArgumentException("pickOne(args) requires at least 1 argument") }

        ctx.random.nextInt(args.size).let {
            return args[it]
        }
    }
}