package fr.cvlaminck.gradle.versioning.task

import fr.cvlaminck.gradle.versioning.manager.ArtifactIdTemplateSelector
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdUpdaterManager
import fr.cvlaminck.gradle.versioning.manager.VcsInformationExtractorManager
import fr.cvlaminck.gradle.versioning.manager.template.ArtifactIdGenerator
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

open class UpdateArtifactIdTask : DefaultTask() {

    private lateinit var versioningExtension: VersioningExtension
    private lateinit var artifactIdTemplateSelector: ArtifactIdTemplateSelector
    private lateinit var vcsInformationExtractorManager: VcsInformationExtractorManager
    private lateinit var artifactIdGenerator: ArtifactIdGenerator
    private lateinit var artifactIdUpdaterManager: ArtifactIdUpdaterManager

    @TaskAction
    fun setVersion() {
        val vcsInformation = vcsInformationExtractorManager.extractInformation(project)
        log.info("Extracted vcs information: {}", vcsInformation) // FIXME to debug

        val template = artifactIdTemplateSelector.selectBestEligibleTemplate(versioningExtension, vcsInformation)
        if (template == null) {
            log.info("No template found that matched vcs information: Artifact id will be kept untouched.")
            return
        }
        log.info("Selected version template: {}", template) // FIXME to debug

        val artifactId = artifactIdGenerator.generate(project, versioningExtension, template)
        if (artifactId.empty) {
            log.info("No part of the artifact id are being updated by the template: Artifact id will be kept untouched.")
            return
        }
        log.info("Generated artifact id: {}", artifactId)

        artifactIdUpdaterManager.updateArtifactId(project, template, null, null, artifactId.version) // FIXME Implements name and group
    }

    companion object {
        private val log = LoggerFactory.getLogger(UpdateArtifactIdTask::class.java)
        fun create(project: Project,
                   taskName: String,
                   versioningExtension: VersioningExtension,
                   versionTemplateSelector: ArtifactIdTemplateSelector,
                   vcsInformationExtractorManager: VcsInformationExtractorManager,
                   versionGenerator: ArtifactIdGenerator, artifactIdUpdaterManager: ArtifactIdUpdaterManager): UpdateArtifactIdTask {
            val task = project.tasks.create(taskName, UpdateArtifactIdTask::class.java)
            task.versioningExtension = versioningExtension
            task.artifactIdTemplateSelector = versionTemplateSelector
            task.vcsInformationExtractorManager = vcsInformationExtractorManager
            task.artifactIdGenerator = versionGenerator
            task.artifactIdUpdaterManager = artifactIdUpdaterManager
            return task
        }
    }
}
