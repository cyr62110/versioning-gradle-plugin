package fr.cvlaminck.gradle.versioning.manager

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.MustacheFactory
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
                generateVersion(template, scopes)
        )
    }

    private fun generateVersion(template: ArtifactIdTemplate, scopes: Map<String, Any>): String {
        val generator = mustacheFactory.compile(StringReader(template.template), template.name + "-version")
        val outputWriter = StringWriter()
        generator.execute(outputWriter, scopes)
        return outputWriter.toString()
    }

    private fun getScopes(project: Project, versioningExtension: VersioningExtension): Map<String, Any> {
        // FIXME Set scopes to get ext, version, group, name, version from project
        return getProjectScopes(project)
    }

    private fun getProjectScopes(project: Project): Map<String, Any> {
        return mapOf(
                "version" to project.version,
                "group" to project.group,
                "name" to project.name
        )
    }
}
