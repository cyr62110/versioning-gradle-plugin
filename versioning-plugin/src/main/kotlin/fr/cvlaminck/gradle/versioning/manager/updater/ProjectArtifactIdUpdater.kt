package fr.cvlaminck.gradle.versioning.manager.updater

import org.gradle.api.Project

class ProjectArtifactIdUpdater(
        private val project: Project
) : ArtifactIdUpdater {

    override fun updateArtifactId(group: String?, name: String?, version: String?) {
        if (group != null) {
            project.group = group
        }
        if (version != null) {
            project.version = version
        }
    }
}
