package fr.cvlaminck.gradle.versioning.manager.template.fn

import com.github.mustachejava.DefaultMustacheFactory
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import fr.cvlaminck.gradle.versioning.manager.template.TemplateScopesBuilder
import fr.cvlaminck.gradle.versioning.manager.template.fn.str.CapitalizeTemplateFunction
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Project
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CompilingTemplateFunctionTest {

    @Test
    fun apply() {
        val project = mock<Project> {
            on { it.name } doReturn "test"
        }
        val versioningExtension = mock<VersioningExtension>()
        val mustacheFactory = DefaultMustacheFactory()
        val scopesBuilder = TemplateScopesBuilder(project, versioningExtension, mustacheFactory)

        val capitalizeTemplateFunction = CapitalizeTemplateFunction(mustacheFactory, scopesBuilder)

        // No template in the value
        assertEquals("Test", capitalizeTemplateFunction.apply("test"))

        // Template in the value
        assertEquals("Test", capitalizeTemplateFunction.apply("{{name}}"))

        // Function in the value
        assertEquals("Test", capitalizeTemplateFunction.apply("{{#lowerCase}}TEST{{/lowerCase}}"))
    }
}
