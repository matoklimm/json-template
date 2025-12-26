package io.github.klimmmax.api

import io.github.klimmmax.core.ExpressionParser
import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.core.FunctionRegistry
import io.github.klimmmax.core.TemplateRenderer
import io.github.klimmmax.functions.DefaultFunctions

class JsonTemplateEngineBuilder {

    private var context: ExecutionContext = ExecutionContext()
    private val registry = FunctionRegistry()
    private val parser = ExpressionParser()

    fun register(fn: TemplateFunction): JsonTemplateEngineBuilder = apply {
        registry.register(fn)
    }

    fun withDefault(vararg functions: DefaultFunction): JsonTemplateEngineBuilder = apply {
        functions.forEach {
            registry.register(DefaultFunctions.create(it))
        }
    }

    fun withDefaults(): JsonTemplateEngineBuilder = apply {
        DefaultFunctions.registerAll(registry)
    }

    fun withExecutionContext(ctx: ExecutionContext): JsonTemplateEngineBuilder = apply {
        context = ctx
    }

    fun build(): JsonTemplateEngine {
        val renderer = TemplateRenderer(registry, parser, context)
        return JsonTemplateEngine(renderer)
    }
}