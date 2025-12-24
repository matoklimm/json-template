package io.github.klimmmax.core

internal class TemplateRenderer(
    private val registry: FunctionRegistry,
    private val parser: ExpressionParser,
    private val context: ExecutionContext
) {

    private val regex = Regex("""\$\{([^}]+)}""")

    fun render(template: String): String {
        return regex.replace(template) { match ->
            val rawExpression = match.groupValues[1]
            val expr = parser.parse(rawExpression)
            val fn = registry.get(expr.functionName)
            fn.execute(expr.args, context)
        }
    }
}