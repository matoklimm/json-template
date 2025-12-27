package io.github.klimmmax.core

internal class ExpressionParser {

    fun parse(raw: String): Expression {
        val trimmed = raw.trim()

        val nameEnd = trimmed.indexOf('(')
        if (nameEnd == -1) {
            return Expression(trimmed, mutableListOf())
        }

        val name = trimmed.take(nameEnd)

        val argsPart = trimmed.substring(nameEnd + 1, trimmed.lastIndexOf(')'))
        val args = argsPart
            .split(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableList()

        return Expression(name, args)
    }

}