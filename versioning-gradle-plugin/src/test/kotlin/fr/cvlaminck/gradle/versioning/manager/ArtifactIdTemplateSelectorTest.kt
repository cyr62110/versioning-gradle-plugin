package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VcsInformation
import fr.cvlaminck.gradle.versioning.model.impl.DefaultArtifactIdTemplate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArtifactIdTemplateSelectorTest {

    @Test
    fun findAllEligibleTemplates() {
        val templates: MutableList<ArtifactIdTemplate> = mutableListOf()
        val template1 = DefaultArtifactIdTemplate("test1")
        template1.branchPatterns("master", "feature/.*")
        val template2 = DefaultArtifactIdTemplate("test2")
        template2.branchPatterns("develop")
        val template3 = DefaultArtifactIdTemplate("test3")
        template3.branchPatterns("master", "develop")
        templates.addAll(listOf(template1, template2, template3))

        val selector = ArtifactIdTemplateSelector()

        val vcsInformation = VcsInformation("test", "master", setOf())
        assertEquals(
                listOf(template1, template3),
                selector.findAllEligibleTemplates(templates.asSequence(), vcsInformation).toList())
    }

    @Test
    fun isAtLeastOneBranchPatternMatches() {
        val selector = ArtifactIdTemplateSelector()

        val artifactIdTemplate = DefaultArtifactIdTemplate("test")
        artifactIdTemplate.branchPatterns("master", "feature/.*")

        assertTrue(selector.isAtLeastOneBranchPatternMatches(artifactIdTemplate, infoWithBranch("master")))
        assertTrue(selector.isAtLeastOneBranchPatternMatches(artifactIdTemplate, infoWithBranch("feature/test")))
        assertFalse(selector.isAtLeastOneBranchPatternMatches(artifactIdTemplate, infoWithBranch("develop")))
    }

    private fun infoWithBranch(branch: String) = VcsInformation("test", branch, setOf())
}
