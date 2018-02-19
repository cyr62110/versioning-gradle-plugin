package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.factory.VersionFactory
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdTemplateSelector
import fr.cvlaminck.gradle.versioning.manager.ArtifactIdUpdaterManager
import fr.cvlaminck.gradle.versioning.manager.VcsInformationExtractorManager
import fr.cvlaminck.gradle.versioning.manager.template.ArtifactIdGenerator
import fr.cvlaminck.gradle.versioning.manager.updater.MavenPublicationArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.manager.updater.ProjectArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import fr.cvlaminck.gradle.versioning.model.impl.DefaultArtifactIdTemplateContainer
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersioningExtension
import fr.cvlaminck.gradle.versioning.task.UpdateArtifactIdTask
import fr.cvlaminck.gradle.versioning.task.VersioningTasks
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.internal.reflect.Instantiator
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
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
        project.pluginManager.withPlugin("java") { afterJavaPluginApplied(project, tasks) }
        project.pluginManager.withPlugin("maven-publish") { afterMavenPublishPluginApplied() }
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

    private fun afterJavaPluginApplied(project: Project, tasks: VersioningTasks) {
        artifactIdUpdaterManager.register(ProjectArtifactIdUpdater())
        val jarTask: Task? = project.tasks.getByName("jar")
        jarTask?.dependsOn(tasks.udpateArtifactIdTask)
    }

    private fun afterMavenPublishPluginApplied() {
        artifactIdUpdaterManager.register(MavenPublicationArtifactIdUpdater())
    }

    /**
     * As maven-publish is a model-based plugin, we need to use a RuleSource to modify the generatePom* tasks generated
     * by the model [PublishingExtension].
     */
    open class Rules : RuleSource() {

        @Model
        fun versioning(extensions: ExtensionContainer): VersioningExtension = extensions.getByType(VersioningExtension::class.java)

        /**
         * Find all template that are not restricted to any publications and add all the publication names so they can
         * affect all publications.
         */
        @Mutate
        fun addPublicationNamesInNonRestrictiveTemplates(versioningExtension: VersioningExtension, extensions: ExtensionContainer) {
            val publishingExtension: PublishingExtension = extensions.findByType(PublishingExtension::class.java)
                    ?: return
            val publicationNames = publishingExtension.publications.names.toTypedArray()

            versioningExtension.templateContainer
                    .filter { it.publicationNames.isEmpty() }
                    .forEach { it.publications(*publicationNames) }
        }

        /**
         * Find the generatePom* tasks for each publication targeted in the versioning extension and add a dependency to
         * the [UpdateArtifactIdTask] so we can be sure that the version in the pom file is updated.
         */
        @Mutate
        fun realizePublishingTasks(tasks: ModelMap<Task>, versioningExtension: VersioningExtension, extensions: ExtensionContainer) {
            val publishingExtension: PublishingExtension = extensions.findByType(PublishingExtension::class.java)
                    ?: return

            val updateArtifactIdTask = tasks.get(UPDATE_ARTIFACT_ID_TASK)
            versioningExtension.templateContainer
                    .flatMap { template ->
                        template.publicationNames.map { template to it }
                    }
                    .forEach { pair ->
                        if (publishingExtension.publications.any { it.name == pair.second }) {
                            val generatePomTaskName = "generatePomFileFor${pair.second.capitalize()}Publication"
                            val generatePomTask = tasks.get(generatePomTaskName) as Task
                            generatePomTask.dependsOn(updateArtifactIdTask)
                        } else {
                            log.warn("No matching publication '${pair.second}' found for template '${pair.first.name}'.")
                        }
                    }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersioningPlugin::class.java)

        private val VERSIONING_EXTENSION = "versioning"
        private val UPDATE_ARTIFACT_ID_TASK = "updateArtifactId"
    }
}
