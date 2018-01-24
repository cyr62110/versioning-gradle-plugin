package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.factory.VersionFactory
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersioningExtension
import fr.cvlaminck.gradle.versioning.model.VersionTemplate
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersionTemplateContainer
import fr.cvlaminck.gradle.versioning.task.JavaSetVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.reflect.Instantiator
import org.slf4j.LoggerFactory
import javax.inject.Inject

open class JavaVersioningPlugin @Inject constructor(
        private val instantiator: Instantiator
) : Plugin<Project> {

    private val versionFactory = VersionFactory(instantiator)

    override fun apply(project: Project) {
        val versioningExtension = registerAndConfigureVersioningExtension(project)
        project.pluginManager.withPlugin("java") { afterJavaPluginApplied(project, versioningExtension) }
    }

    private fun registerAndConfigureVersioningExtension(project: Project): VersioningExtension {
        val versionContainer = instantiator.newInstance(DefaultVersionTemplateContainer::class.java, instantiator)
        val versioningExtension = project.extensions.create(
                VersioningExtension::class.java,
                VERSIONING_EXTENSION,
                DefaultVersioningExtension::class.java,
                versionContainer)
        project.extensions.configure(VersioningExtension::class.java) {
            it.versions.registerFactory(VersionTemplate::class.java, versionFactory)
        }
        return versioningExtension
    }

    private fun afterJavaPluginApplied(project: Project, versioningExtension: VersioningExtension) {
        val jarTask: Task? = project.tasks.getByName("jar")
        val setVersionTask = project.tasks.create(SET_VERSION_TASK, JavaSetVersionTask::class.java)
        jarTask?.dependsOn(setVersionTask)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavaVersioningPlugin::class.java)

        private val VERSIONING_EXTENSION = "versioning"
        private val SET_VERSION_TASK = "setVersion"
    }
}
