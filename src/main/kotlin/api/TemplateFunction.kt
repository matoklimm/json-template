package io.github.klimmmax.api

import io.github.klimmmax.core.ExecutionContext

/**
 * A template function that can be used inside `${...}` expressions.
 * The name must case-sensitive match the used function-name inside the expression.
 *
 *  @param args the comma-separated list or arguments within parenthesis functionName(arg1, arg2, ...)
 *
 * Implementations must be stateless or rely only on the provided [ExecutionContext].
 */
interface TemplateFunction {
    val name: String
    fun execute(args: MutableList<String>, ctx: ExecutionContext): String
}