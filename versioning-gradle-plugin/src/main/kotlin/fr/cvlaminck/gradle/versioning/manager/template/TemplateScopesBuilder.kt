package fr.cvlaminck.gradle.versioning.manager.template

import com.github.mustachejava.MustacheFactory
import fr.cvlaminck.gradle.versioning.manager.template.fn.str.CapitalizeTemplateFunction
import fr.cvlaminck.gradle.versioning.manager.template.fn.time.DateTemplateFunction
import fr.cvlaminck.gradle.versioning.manager.template.fn.str.LowerCaseTemplateFunction
import fr.cvlaminck.gradle.versioning.manager.template.fn.str.UpperCaseTemplateFunction
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Project

class TemplateScopesBuilder(
        private val project: Project,
        private val versioningExtension: VersioningExtension,
        private val mustacheFactory: MustacheFactory
) {

    fun build(): List<Map<String, Any>> {
        return listOf(
                getProjectScopes(),
                getStringTemplateFunctions(),
                getTimeTemplateFunctions()
        )
    }

    private fun getProjectScopes(): Map<String, Any> {
        return mapOf(
                "version" to project.version,
                "group" to project.group,
                "name" to project.name
        )
    }


    private fun getStringTemplateFunctions(): Map<String, Any> {
        return mapOf(
                "capitalize" to CapitalizeTemplateFunction(mustacheFactory, this),
                "upperCase" to UpperCaseTemplateFunction(mustacheFactory, this),
                "lowerCase" to LowerCaseTemplateFunction(mustacheFactory, this)
        )
    }

    private fun getTimeTemplateFunctions(): Map<String, Any> {
        return mapOf(
                "date" to DateTemplateFunction()
        )
    }
}
