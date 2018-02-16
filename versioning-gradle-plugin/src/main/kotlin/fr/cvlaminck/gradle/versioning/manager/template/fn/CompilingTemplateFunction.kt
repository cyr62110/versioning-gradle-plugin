package fr.cvlaminck.gradle.versioning.manager.template.fn

import com.github.mustachejava.MustacheFactory
import com.github.mustachejava.TemplateFunction
import fr.cvlaminck.gradle.versioning.manager.template.TemplateScopesBuilder
import java.io.StringReader
import java.io.StringWriter

/**
 * All template function that want to allow replacement of templated value before evaluation
 * must extends this class and implements [applyPostValueCompilation] instead of [apply].
 */
abstract class CompilingTemplateFunction(
        private val mustacheFactory: MustacheFactory,
        private val scopesBuilder: TemplateScopesBuilder
) : TemplateFunction {

    override fun apply(value: String?): String {
        if (value != null && value.contains("{{") && value.contains("}}")) {
            val mustache = mustacheFactory.compile(StringReader(value), value)
            val writer = StringWriter()
            mustache.execute(writer, scopesBuilder.build())
            return applyPostValueCompilation(writer.toString())
        } else {
            return applyPostValueCompilation(value)
        }
    }

    abstract fun applyPostValueCompilation(value: String?): String
}
