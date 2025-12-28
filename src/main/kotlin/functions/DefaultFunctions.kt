package io.github.matoklimm.functions

import io.github.matoklimm.api.DefaultFunction
import io.github.matoklimm.api.TemplateFunction
import io.github.matoklimm.core.FunctionRegistry

internal object DefaultFunctions {

    fun create(fn: DefaultFunction): TemplateFunction =
        when (fn) {
            DefaultFunction.RANDOM_BOOL -> RandomBool()
            DefaultFunction.RANDOM_INT -> RandomInt()
            DefaultFunction.RANDOM_FLOAT -> RandomFloat()
            DefaultFunction.RANDOM_STRING -> RandomString()
            DefaultFunction.RANDOM_TIMESTAMP -> RandomTimestamp()
            DefaultFunction.RANDOM_UUID -> RandomUuid()
            DefaultFunction.TIMESTAMP -> Timestamp()
            DefaultFunction.COUNT -> Count()
            DefaultFunction.PICK_ONE -> PickOne()
        }

    fun registerAll(registry: FunctionRegistry) {
        DefaultFunction.values().forEach {
            registry.register(create(it))
        }
    }

}