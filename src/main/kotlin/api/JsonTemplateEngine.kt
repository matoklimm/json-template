package io.github.klimmmax.api

import io.github.klimmmax.core.TemplateRenderer

class JsonTemplateEngine internal constructor(
    private val renderer: TemplateRenderer
) {
    fun render(template: String): String = renderer.render(template)
}