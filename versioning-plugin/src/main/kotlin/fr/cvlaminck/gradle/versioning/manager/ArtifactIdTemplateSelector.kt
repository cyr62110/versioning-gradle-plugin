package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.model.VcsInformation
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension

/**
 * Manager performing the selection of the template to use to generate the version name.
 *
 * The rules for a template to be eligible are:
 * - One of the branch pattern must match the current branch.
 * - TODO: Add more meaningfull
 *
 * If multiple templates are eligible, the first one that has been registered will be selected.
 */
class ArtifactIdTemplateSelector {

    fun selectBestEligibleTemplate(versioningExtension: VersioningExtension, vcsInformation: VcsInformation): ArtifactIdTemplate? {
        val templates = findAllEligibleTemplates(versioningExtension, vcsInformation)
        return templates.firstOrNull()
    }

    private fun findAllEligibleTemplates(versioningExtension: VersioningExtension, vcsInformation: VcsInformation): List<ArtifactIdTemplate> {
        return versioningExtension.templateContainer
                .filter { isAtLeastOneBranchPatternMatches(it, vcsInformation) }
    }

    private fun isAtLeastOneBranchPatternMatches(versionTemplate: ArtifactIdTemplate, vcsInformation: VcsInformation): Boolean
            = versionTemplate.branchPatterns
            .map(String::toRegex)
            .any { vcsInformation.branchName.matches(it) }
}
