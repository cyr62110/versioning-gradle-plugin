package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.model.VcsInformation
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.slf4j.LoggerFactory

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
        log.debug("Templates: {}", versioningExtension.templateContainer.toList())
        val templates = findAllEligibleTemplates(
                versioningExtension.templateContainer.asSequence(),
                vcsInformation)
        return templates.firstOrNull()
    }

    internal fun findAllEligibleTemplates(templates: Sequence<ArtifactIdTemplate>, vcsInformation: VcsInformation): Sequence<ArtifactIdTemplate>
        = templates.filter { isAtLeastOneBranchPatternMatches(it, vcsInformation) }

    internal fun isAtLeastOneBranchPatternMatches(template: ArtifactIdTemplate, vcsInformation: VcsInformation): Boolean
            = template.branchPatterns
            .map(String::toRegex)
            .any { vcsInformation.branchName.matches(it) }

    companion object {
        private val log = LoggerFactory.getLogger(ArtifactIdTemplateSelector::class.java)
    }
}
