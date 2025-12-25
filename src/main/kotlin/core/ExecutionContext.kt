package io.github.klimmmax.core

import kotlin.random.Random
import java.time.Clock


class ExecutionContext internal constructor(
    val random: Random = Random.Default,
    val clock: Clock = Clock.systemUTC(),
    val state: MutableMap<String, Any> = mutableMapOf()
)