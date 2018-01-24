package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Action

/**
 *
 */
interface VersioningExtension {

    val defaultVersion: String

    fun defaultVersion(version: String)

    /**
     * Container of all registered [VersionTemplate].
     */
    val templateContainer: VersionTemplateContainer

    /**
     * Register new templates.
     */
    fun templates(configure: Action<in VersionTemplateContainer>)
}
