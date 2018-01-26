package fr.cvlaminck.gradle.versioning.task

import fr.cvlaminck.gradle.versioning.manager.VcsInformationExtractorManager
import fr.cvlaminck.gradle.versioning.manager.VersionTemplateSelector
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

open class JavaSetVersionTask : DefaultTask() {

    private lateinit var versioningExtension: VersioningExtension
    private lateinit var versionTemplateSelector: VersionTemplateSelector
    private lateinit var vcsInformationExtractorManager: VcsInformationExtractorManager

    @TaskAction
    fun setVersion() {
        val vcsInformation = vcsInformationExtractorManager.extractInformation(project)
        log.info("Extracted vcs information: {}", vcsInformation) // FIXME to debug

        val template = versionTemplateSelector.selectBestEligibleTemplate(versioningExtension, vcsInformation)
        log.info("Selected version template: {}", template)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavaSetVersionTask::class.java)
        fun create(project: Project,
                   taskName: String,
                   versioningExtension: VersioningExtension,
                   versionTemplateSelector: VersionTemplateSelector,
                   vcsInformationExtractorManager: VcsInformationExtractorManager): JavaSetVersionTask {
            val task = project.tasks.create(taskName, JavaSetVersionTask::class.java)
            task.versioningExtension = versioningExtension
            task.versionTemplateSelector = versionTemplateSelector
            task.vcsInformationExtractorManager = vcsInformationExtractorManager
            return task
        }
    }
}
