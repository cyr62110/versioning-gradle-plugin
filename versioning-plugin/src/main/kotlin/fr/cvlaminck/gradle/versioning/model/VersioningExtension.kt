package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Action

interface VersioningExtension {

    val defaultVersion: String

    fun defaultVersion(version: String)

    val versions: VersionContainer

    fun versions(action: Action<out VersionContainer>)
}
