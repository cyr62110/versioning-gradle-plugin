package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.manager.updater.ArtifactIdUpdater
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import org.gradle.api.Project

class ArtifactIdUpdaterManager {

    private val updaters: MutableSet<ArtifactIdUpdater> = mutableSetOf()

    fun updateArtifactId(project: Project, template: ArtifactIdTemplate, group: String?, name: String?, version: String?) {
        updaters.forEach {
            it.updateArtifactId(project, template, group, name, version)
        }
    }

    fun register(artifactIdUpdater: ArtifactIdUpdater) {
        updaters.add(artifactIdUpdater)
    }
}
