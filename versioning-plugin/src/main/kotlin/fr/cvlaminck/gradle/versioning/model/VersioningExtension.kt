package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Action

interface VersioningExtension {

    val defaultVersion: String

    fun defaultVersion(version: String)

    val versions: VersionTemplateContainer

    fun versions(configure: Action<in VersionTemplateContainer>)
}
