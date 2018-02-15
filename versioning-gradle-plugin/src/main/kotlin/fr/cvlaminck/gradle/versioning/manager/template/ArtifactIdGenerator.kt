package fr.cvlaminck.gradle.versioning.manager.template

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
import fr.cvlaminck.gradle.versioning.manager.template.fn.CapitalizeTemplateFunction
import fr.cvlaminck.gradle.versioning.manager.template.fn.DateTemplateFunction
import fr.cvlaminck.gradle.versioning.model.ArtifactId
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Project
import java.io.StringReader
import java.io.StringWriter

class ArtifactIdGenerator {

    private val mustacheFactory: MustacheFactory = DefaultMustacheFactory()

    fun generate(project: Project, versioningExtension: VersioningExtension, template: ArtifactIdTemplate): ArtifactId {
        val scopes = getScopes(project, versioningExtension)
        return ArtifactId(
                null, // FIXME Implements
                null, // FIXME Implements
                generate("${template.name}-version", template.template, scopes)
        )
    }

    internal fun generate(name: String, template: String, scopes: List<Map<String, Any>>): String {
        val generator = mustacheFactory.compile(StringReader(template), name)
        val outputWriter = StringWriter()
        generator.execute(outputWriter, scopes)
        return outputWriter.toString()
    }

    internal fun getScopes(project: Project, versioningExtension: VersioningExtension): List<Map<String, Any>> {
        return listOf(
                getProjectScopes(project),
                getDefaultTemplateFunction()
        )
    }

    private fun getProjectScopes(project: Project): Map<String, Any> {
        return mapOf(
                "version" to project.version,
                "group" to project.group,
                "name" to project.name
        )
    }

    /**
     * Returns all the template functions that are supported by the plugin out-of-the-box.
     */
    private fun getDefaultTemplateFunction(): Map<String, Any> {
        return mapOf(
                "capitalize" to CapitalizeTemplateFunction(),
                "date" to DateTemplateFunction()
        )
    }
}
