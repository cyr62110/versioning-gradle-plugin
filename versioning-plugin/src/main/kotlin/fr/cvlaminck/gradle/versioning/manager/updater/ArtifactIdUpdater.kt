package fr.cvlaminck.gradle.versioning.manager.updater

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import org.gradle.api.Project

interface ArtifactIdUpdater {
    fun updateArtifactId(project: Project, template: ArtifactIdTemplate, group: String?, name: String?, version: String?)
}
