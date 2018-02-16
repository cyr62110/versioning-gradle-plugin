package fr.cvlaminck.gradle.versioning.manager.template.fn.str

import com.github.mustachejava.MustacheFactory
import fr.cvlaminck.gradle.versioning.manager.template.TemplateScopesBuilder
import fr.cvlaminck.gradle.versioning.manager.template.fn.CompilingTemplateFunction

class CapitalizeTemplateFunction(
        mustacheFactory: MustacheFactory,
        scopesBuilder: TemplateScopesBuilder
) : CompilingTemplateFunction(mustacheFactory, scopesBuilder) {

    override fun applyPostValueCompilation(value: String?): String = (value ?: "").capitalize()
}
