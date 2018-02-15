package fr.cvlaminck.gradle.versioning.manager.template

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArtifactIdGeneratorTest {

    @Test
    fun generate_withProjectVariable() {
        val project = mock<Project> {
            on { it.name } doReturn "test"
            on { it.group } doReturn "fr.cvlaminck.gradle.versioning"
            on { it.version } doReturn "1.0"
        }
        val versioningExtension = mock<VersioningExtension>()

        val generator = ArtifactIdGenerator()
        assertEquals("test", generator.generate("test", "{{name}}", generator.getScopes(project, versioningExtension)))
        assertEquals("fr.cvlaminck.gradle.versioning", generator.generate("test", "{{group}}", generator.getScopes(project, versioningExtension)))
        assertEquals("1.0", generator.generate("test", "{{version}}", generator.getScopes(project, versioningExtension)))
    }

    @Test
    fun generate_withStandardTemplateFunction() {
        val project = mock<Project> {
            on { it.name } doReturn "test"
            on { it.group } doReturn "fr.cvlaminck.gradle.versioning"
            on { it.version } doReturn "1.0"
        }
        val versioningExtension = mock<VersioningExtension>()

        val generator = ArtifactIdGenerator()
        assertEquals("Test", generator.generate("test", "{{#capitalize}}test{{/capitalize}}", generator.getScopes(project, versioningExtension)))
    }
}
