package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.factory.VersionFactory
import fr.cvlaminck.gradle.versioning.model.Version
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import fr.cvlaminck.gradle.versioning.task.JavaSetVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.AppliedPlugin
import org.slf4j.LoggerFactory

open class JavaVersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.configure(VersioningExtension::class.java) {
            it.versions.registerFactory(Version::class.java, VersionFactory())
        }

        project.pluginManager.withPlugin("java") { plugin -> afterJavaPluginApplied(project, plugin) }
    }

    private fun afterJavaPluginApplied(project: Project, plugin: AppliedPlugin) {
        val jarTask: Task? = project.tasks.getByName("jar")
        val setVersionTask = project.tasks.create(SET_VERSION_TASK, JavaSetVersionTask::class.java)
        jarTask?.dependsOn(setVersionTask)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavaVersioningPlugin::class.java)

        private val SET_VERSION_TASK = "setVersion"
    }
}
