package io.github.klimmmax.functions

import io.github.klimmmax.api.DefaultFunction
import io.github.klimmmax.api.TemplateFunction
import io.github.klimmmax.core.FunctionRegistry

internal object DefaultFunctions {

    fun create(fn: DefaultFunction): TemplateFunction =
        when (fn) {
            DefaultFunction.RANDOM_BOOL -> RandomBool()
            DefaultFunction.RANDOM_INT -> RandomInt()
            DefaultFunction.RANDOM_FLOAT -> RandomFloat()
            DefaultFunction.RANDOM_STRING -> RandomString()
            DefaultFunction.RANDOM_TIMESTAMP -> RandomTimestamp()
            DefaultFunction.TIMESTAMP -> Timestamp()
            DefaultFunction.COUNT -> Count()
        }

    fun registerAll(registry: FunctionRegistry) {
        DefaultFunction.values().forEach {
            registry.register(create(it))
        }
    }

}