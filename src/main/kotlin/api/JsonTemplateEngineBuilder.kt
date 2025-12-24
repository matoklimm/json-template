package io.github.klimmmax.api

import io.github.klimmmax.core.ExpressionParser
import io.github.klimmmax.core.ExecutionContext
import io.github.klimmmax.core.FunctionRegistry
import io.github.klimmmax.core.TemplateRenderer

class JsonTemplateEngineBuilder {

    private val registry = FunctionRegistry()
    private val context = ExecutionContext()
    private val parser = ExpressionParser()

    fun register(fn: TemplateFunction): JsonTemplateEngineBuilder = apply {
        registry.register(fn)
    }

    fun build(): JsonTemplateEngine {
        val renderer = TemplateRenderer(registry, parser, context)
        return JsonTemplateEngine(renderer)
    }
}