package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Action

/**
 *
 */
interface VersioningExtension {

    /**
     * Container of all registered [ArtifactIdTemplate].
     */
    val templateContainer: ArtifactIdTemplateContainer

    /**
     * Register new templates.
     */
    fun templates(configure: Action<in ArtifactIdTemplateContainer>)
}
