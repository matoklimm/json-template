package io.github.klimmmax.core

import kotlin.random.Random


class ExecutionContext internal constructor(
    val random: Random = Random.Default,
    val state: MutableMap<String, Any> = mutableMapOf()
)