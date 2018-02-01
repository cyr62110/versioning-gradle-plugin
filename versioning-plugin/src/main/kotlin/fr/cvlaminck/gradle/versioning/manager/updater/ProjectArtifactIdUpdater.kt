package fr.cvlaminck.gradle.versioning.manager.updater

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import org.gradle.api.Project

class ProjectArtifactIdUpdater : ArtifactIdUpdater {

    override fun updateArtifactId(project: Project, template: ArtifactIdTemplate, group: String?, name: String?, version: String?) {
        if (group != null) {
            project.group = group
        }
        if (version != null) {
            project.version = version
        }
    }
}
