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

    /**
     * Registers a custom template function provided by library user by implementing [TemplateFunction].
     *
     * @throws IllegalArgumentException if a function with the same name
     *         is already registered.
     */
    fun register(fn: TemplateFunction): JsonTemplateEngineBuilder = apply {
        registry.register(fn)
    }

    /**
     * Registers a subset of the built-in default functions.
     *
     * @param functions the default functions to register also see [DefaultFunction]
     */
    fun withDefault(vararg functions: DefaultFunction): JsonTemplateEngineBuilder = apply {
        functions.forEach {
            registry.register(DefaultFunctions.create(it))
        }
    }

    /**
     * Registers all built-in default functions.
     */
    fun withDefaults(): JsonTemplateEngineBuilder = apply {
        DefaultFunctions.registerAll(registry)
    }

    /**
     * Overrides the execution context used during template rendering.
     *
     * This can be used to provide a custom Random instance or shared state.
     */
    fun withExecutionContext(ctx: ExecutionContext): JsonTemplateEngineBuilder = apply {
        context = ctx
    }

    fun build(): JsonTemplateEngine {
        val renderer = TemplateRenderer(registry, parser, context)
        return JsonTemplateEngine(renderer)
    }
}