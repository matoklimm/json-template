package io.github.klimmmax.core

import kotlin.random.Random
import java.time.Clock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


class ExecutionContext internal constructor(
    val random: Random = Random.Default,
    val clock: Clock = Clock.systemUTC(),
    val state: ConcurrentMap<String, Any> = ConcurrentHashMap()
)