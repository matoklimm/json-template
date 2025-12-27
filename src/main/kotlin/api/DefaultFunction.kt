/*
 * Copyright 2025 Maximilian Klimm
 *
 * Licensed under the Apache License, Version 2.0
 */
package io.github.klimmmax.api

/**
 * The list of default functions. Each can be registered individually via [JsonTemplateEngineBuilder.withDefault]
 * */
enum class DefaultFunction {
    RANDOM_BOOL,
    RANDOM_INT,
    RANDOM_FLOAT,
    RANDOM_STRING,
    RANDOM_TIMESTAMP,
    RANDOM_UUID,
    TIMESTAMP,
    COUNT
}