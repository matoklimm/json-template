package io.github.klimmmax.core

import io.github.klimmmax.api.TemplateFunction

internal class FunctionRegistry {

    private val functions = mutableMapOf<String, TemplateFunction>()

    fun register(templateFunction: TemplateFunction) {
        check(!functions.containsKey(templateFunction.name)) {
            "Function '${templateFunction.name}' already registered!"
        }

        functions[templateFunction.name] = templateFunction
    }

    fun get(name: String): TemplateFunction = functions[name] ?: error("Unknown function '$name'")
}