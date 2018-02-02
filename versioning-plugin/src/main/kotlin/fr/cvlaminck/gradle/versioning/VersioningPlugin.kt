package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.factory.VersionFactory
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdUpdaterManager
import fr.cvlaminck.gradle.versioning.manager.VcsInformationExtractorManager
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdGenerator
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdTemplateSelector
import fr.cvlaminck.gradle.versioning.manager.updater.MavenPublicationArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.manager.updater.ProjectArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersioningExtension
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import fr.cvlaminck.gradle.versioning.model.impl.DefaultArtifactIdTemplateContainer
import fr.cvlaminck.gradle.versioning.task.UpdateArtifactIdTask
import fr.cvlaminck.gradle.versioning.task.VersioningTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.slf4j.LoggerFactory
import javax.inject.Inject

open class VersioningPlugin @Inject constructor(
        private val instantiator: Instantiator
) : Plugin<Project> {

    private val versionFactory = VersionFactory(instantiator)
    private val vcsInformationExtractorManager = VcsInformationExtractorManager()
    private val versionTemplateSelector = ArtifactIdTemplateSelector()
    private val artifactIdGenerator = ArtifactIdGenerator()
    private val artifactIdUpdaterManager = ArtifactIdUpdaterManager()

    override fun apply(project: Project) {
        val versioningExtension = registerAndConfigureVersioningExtension(project)
        val tasks = registerTasks(project, versioningExtension)
        project.pluginManager.withPlugin("java") { afterJavaPluginApplied(project, tasks, versioningExtension) }
        project.pluginManager.withPlugin("maven-publish") { afterMavenPublishPluginApplied(project, tasks, versioningExtension) }
    }

    private fun registerAndConfigureVersioningExtension(project: Project): VersioningExtension {
        val versionContainer = instantiator.newInstance(DefaultArtifactIdTemplateContainer::class.java, instantiator)
        val versioningExtension = project.extensions.create(
                VersioningExtension::class.java,
                VERSIONING_EXTENSION,
                DefaultVersioningExtension::class.java,
                versionContainer)
        project.extensions.configure(VersioningExtension::class.java) {
            it.templateContainer.registerFactory(ArtifactIdTemplate::class.java, versionFactory)
        }
        return versioningExtension
    }

    private fun registerTasks(project: Project, versioningExtension: VersioningExtension): VersioningTasks {
        val updateArtifactIdTask = UpdateArtifactIdTask.create(project, UPDATE_ARTIFACT_ID_TASK, versioningExtension,
                versionTemplateSelector, vcsInformationExtractorManager, artifactIdGenerator, artifactIdUpdaterManager)
        return VersioningTasks(
                updateArtifactIdTask
        )
    }

    private fun afterJavaPluginApplied(project: Project, tasks: VersioningTasks, versioningExtension: VersioningExtension) {
        artifactIdUpdaterManager.register(ProjectArtifactIdUpdater())
        val jarTask: Task? = project.tasks.getByName("jar")
        jarTask?.dependsOn(tasks.udpateArtifactIdTask)
    }

    private fun afterMavenPublishPluginApplied(project: Project , tasks: VersioningTasks, versioningExtension: VersioningExtension) {
        artifactIdUpdaterManager.register(MavenPublicationArtifactIdUpdater())
    }

    open inner class Rules : RuleSource() {

        @Mutate
        fun realizePublishingTasks(tasks: ModelMap<Task>) {
            log.debug("tasks: ${tasks}")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersioningPlugin::class.java)

        private val VERSIONING_EXTENSION = "versioning"
        private val UPDATE_ARTIFACT_ID_TASK = "updateArtifactId"
    }
}
