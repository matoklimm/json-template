# Kotlin/Java JsonTemplate

A **lightweight Kotlin/Java library** for rendering JSON-like templates
by **replacing function expressions with generated values**.

This library is intentionally simple:
- no JSON validation 
- no custom DSL 
- no reflection 
- no magic

It performs **plain string replacement** based on `${...}` expressions.

------------------------------------------------------------------------

## Motivation

When building IoT platforms, tests, simulators or demo pipelines, you
often need to **generate JSON messages dynamically**:

-   random sensor values
-   timestamps
-   counters
-   simple stateful values

json-template focuses exactly on that use case:

> **Replace function calls embedded in a string with generated values.**

Nothing more. ✅

------------------------------------------------------------------------

## Core Idea 🌀

Given a template string like:

``` json
{
  "deviceId": "sensor-1",
  "temperature": ${randomInt(15,30)},
  "active": ${randomBool()},
  "timestamp": "${timestamp()}"
}
```

The engine will:

1.  find all `${...}` expressions
2.  parse the function name and arguments
3.  execute the corresponding function
4.  replace the expression with the returned string

and produce the following output string
``` json
{
  "deviceId": "sensor-1",
  "temperature": 22,
  "active": true,
  "timestamp": "2025-12-24T13:37:00Z"
}
```

➡️ **No JSON parsing is involved**
➡️ **The result is just a rendered string**

------------------------------------------------------------------------

## What this library is **not**

-   ❌ No JSON schema validation
-   ❌ No guarantee that output is valid JSON
-   ❌ No expression language
-   ❌ No reflection or dynamic class loading

If you need strict JSON validation or expressive DSL, this library is
probably **not** what you want.

------------------------------------------------------------------------

## Getting Started 🚀

### Dependency

``` xml
<dependency>
  <groupId>io.github.matoklimm</groupId>
  <artifactId>json-template</artifactId>
  <version>1.4</version>
</dependency>
```

Setting up the code is as easy as:
```kotlin
val template = JsonTemplateEngineBuilder()
        .withDefaults()
        .build()

val fileContent = object {}::class.java
    .getResource("/simple_sample.json")
    ?.readText()
    ?: error("Resource simple_sample.json not found")
    
template.render(fileContent)
```

## Registering Built-in Functions 🔧

If you want to use **ALL** built-in functions simply call `.withDefaults()` on the builder like shown above. 💯

<details>

<summary>You can also just register the built-in functions you really need</summary>

You can simply register only those you need with `.withDefault(array of DefaultFunction)`.
For that, refer to [`DefaultFunction`](src/main/kotlin/api/DefaultFunction.kt)
```kotlin
class YourApplication {
    
    val jsonTemplateEngine = JsonTemplateEngineBuilder()
        .withDefault(
            DefaultFunction.RANDOM_INT,
            DefaultFunction.RANDOM_TIMESTAMP
        )
        .build()
}
```

</details>

------------------------------------------------------------------------

## Built-in Functions ƒ

### randomInt
    passing no args will simply call random.nextInt() which returns any Integer from Int.MIN_VALUE and Int.MAX_VALUE
    ${randomInt()}
    
    passing one arg will be used as max (or min) value whilst the first random param will be always 0
    ${randomInt(max)}
    ${randomInt(42)}                    -> 13
    ${randomInt(-6)}                    -> -3

    passing two args will return any random Int between those values
    ${randomInt(min, max)}
    ${randomInt(-13,37)}                -> 12

### randomFloat
    passing no args will simply call random.nextFloat() which returns any Float between 0 and 1 and rounded to 2 decimals
    ${randomFloat()}
    
    passing one arg will be used as max (or min) value whilst the first random param will be always 0 and rounded to 2 decimals
    ${randomFloat(max)}
    ${randomFloat(2)}                   -> 0.67
    ${randomFloat(-6)}                  -> -3.33

    passing two args will return any random Float between those values
    ${randomFloat(min, max)}
    ${randomFloat(-13,37)}              -> 13.37

    passing three args will return any random Float between the first two argumenst and round to decimals of the third argument
    ${randomFloat(min, max, decimals)}
    ${randomFloat(-13,37, 5)}           -> 3.14159

### randomBool
    randomBool() is pretty self-explaining and returns a bool :D

    ${randomBool()}

### randomString
    randomString() will return a random string consisting of a-z,A-Z and 0-9
    **Note** This will not put quotation marks around the returned string so in your JSON template you might want to
    put them there before calling this function

    passing no args will return a string with length of 12
    ${randomString()}           -> sTaRwArS4EvR

    passing one arg will determine the max length of the random string
    ${randomString(maxLength)}
    ${randomString(8)}          -> sTaRwArS
    
    passing two args will determine the max length of the random string from the first argument
    as well as putting the passed prefix argument in front of every random generation
    ${randomString(maxLength, prefix)}
    ${randomString(8,I love )}  -> I love sTaRwArS

### timestamp
    timestamp() will always return a OffsetDateTime.now() as String in UTC Timezone

    ${timestamp()}              -> 2025-12-24T13:37:00Z

### randomTimestamp
    randomTimestamp() will choose a random point in time between the passed arguments

    passing one argument will create a random timestamp between now and the provided timestamp despite
    the argument being in the past or future
    ${randomTimestamp(upperBound)}
    ${randomTimestamp(2025-12-25T13:37:00Z)}                        -> 2025-12-24T14:29:08.233Z
    (now() is 2025-12-24T13:45:00Z)
    
    passing two arguments will choose a random timestamp bewtween the the provided borders
    ${randomTimestamp(start, end)}
    ${randomTimestamp(2025-12-23T01:42:00Z,2025-12-25T13:37:00Z)}   -> 2025-12-25T02:23:10.012Z

### randomUuid
    randomUuid() is pretty self-explaining and returns a random UUID :D

    ${randomUuid()}

### count
    count() is a stateful function will increase its value throughout multiple calls within one template
    **Note** We are using LONG to store the current counter value and have no overflow protection so if you e.g. are at
    9223372036854775806 and add a few steps you will overflow the LONG value

    passing no args will have a default behaviour of starting at 1 and increase each invocation by 1
    ${count()}                  -> 1, 2, 3, 4, ...
    
    passing one argument will result in adjusting the steps of the counter whilst still starting at 0 before the fisrt call
    **Note** the step arg is evaluated throughout each function invocation so if you want to step for example by 2
    each count() call has to have the 2 as first argument
    ${count(step)}
    ${count(3)}                 -> 3, 6, 9, 12, ...

    passing two args will result the counter to start at the first argument for the very first function invocation.
    After that the provided step from each invocation will be used
    ${count(start, step)}
    ${count(13, 2)}             -> 13, 15, 17, 19, ...

------------------------------------------------------------------------

## Custom Functions

Implement the `TemplateFunction` interface and register it via the builder.

```kotlin
class IdFunction: TemplateFunction {
    override val name = "uuid"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        return UUID.randomUUID().toString()
    }
}

val jsonTemplate = JsonTemplateEngineBuilder()
    .withDefaults()
    .register(IdFunction())
    .build()

val template = """
        {
            "id": ${uuid()}
        }
    """.trimIndent()

jsonTemplate.render(template)
```

This will result in 
>{ "id": d6ba7e90-1290-466c-800b-7064871f4b86 }

So you can implement all further functionality you need yourself.
All arguments you want to pass to the `args: MutableList<String>` must be within parenthesis and comma separated for example

> ${customFunction(1,2,3)}

```kotlin
class CustomFunction: TemplateFunction {
    override val name = "customFunction"

    override fun execute(args: MutableList<String>, ctx: ExecutionContext): String {
        println(args) // 1,2,3
        return "your processed string"
    }
}
```

### Stateful functions
If you want to build a stateful function you can use the `ExecutionContext` for that. It has a `state` which is a simple
`ConcurrentHashMap<String, Any>`. Use your function key and store whatever you need to compute your stateful invocation.

For example on the first invocation we generate a random UUID which is reused for any further calls in the template:

```kotlin
class SessionId : TemplateFunction {
    override val name = "sessionId"

    override fun execute(args: List<String>, ctx: ExecutionContext): String {
        val existing = ctx.state[name]
        if (existing != null) {
            return existing.toString()
        }

        val newId = UUID.randomUUID().toString()
        ctx.state[name] = newId
        return newId
    }
}
```

------------------------------------------------------------------------

## Summary

✔️ extremely lightweight\
✔️ no reflection\
✔️ no DSL\
✔️ no JSON parsing\
✔️ easy to extend\
✔️ ideal for IoT simulators, tests and demos


## Contribution 🖥️

If you find any bugs 🐞 or have enhancement ideas ✨ which align with the lightweight idea of this library, 
feel free to open an issue on GitHub. ♥️

## License 🧾

This project is licensed under the Apache License 2.0.
