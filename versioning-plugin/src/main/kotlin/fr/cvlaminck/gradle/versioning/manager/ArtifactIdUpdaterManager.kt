package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.manager.updater.ArtifactIdUpdater

class ArtifactIdUpdaterManager {

    private val updaters: MutableSet<ArtifactIdUpdater> = mutableSetOf()

    fun updateArtifactId(group: String?, name: String?, version: String?) {
        updaters.forEach {
            it.updateArtifactId(group, name, version)
        }
    }

    fun register(artifactIdUpdater: ArtifactIdUpdater) {
        updaters.add(artifactIdUpdater)
    }
}
