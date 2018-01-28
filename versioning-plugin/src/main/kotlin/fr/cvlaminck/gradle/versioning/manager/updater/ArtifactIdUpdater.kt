package fr.cvlaminck.gradle.versioning.manager.updater

interface ArtifactIdUpdater {
    fun updateArtifactId(group: String?, name: String?, version: String?)
}
