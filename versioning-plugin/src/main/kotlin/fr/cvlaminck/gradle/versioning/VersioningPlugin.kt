package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.factory.VersionFactory
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdUpdaterManager
import fr.cvlaminck.gradle.versioning.manager.VcsInformationExtractorManager
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdGenerator
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdTemplateSelector
import fr.cvlaminck.gradle.versioning.manager.updater.ProjectArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersioningExtension
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import fr.cvlaminck.gradle.versioning.model.impl.DefaultArtifactIdTemplateContainer
import fr.cvlaminck.gradle.versioning.task.UpdateArtifactIdTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.Instantiator
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
        artifactIdUpdaterManager.register(ProjectArtifactIdUpdater(project))
        project.pluginManager.withPlugin("java") { afterJavaPluginApplied(project, versioningExtension) }
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

    private fun afterJavaPluginApplied(project: Project, versioningExtension: VersioningExtension) {
        val jarTask: Task? = project.tasks.getByName("jar")
        val setVersionTask = UpdateArtifactIdTask.create(project, UPDATE_ARTIFACT_ID_TASK, versioningExtension,
                versionTemplateSelector, vcsInformationExtractorManager, artifactIdGenerator, artifactIdUpdaterManager)
        jarTask?.dependsOn(setVersionTask)
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersioningPlugin::class.java)

        private val VERSIONING_EXTENSION = "versioning"
        private val UPDATE_ARTIFACT_ID_TASK = "updateArtifactId"
    }
}
