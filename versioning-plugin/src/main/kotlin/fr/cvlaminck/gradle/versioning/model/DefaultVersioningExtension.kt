package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Action

class DefaultVersioningExtension : VersioningExtension {

    private var _defaultVersion: String? = null
    override val defaultVersion: String
        get() = _defaultVersion!!

    override fun defaultVersion(version: String) {
        _defaultVersion = version
    }

    override val versions: VersionContainer
        get() = TODO("Implements")

    override fun versions(action: Action<out VersionContainer>) {
        TODO("Implements")
    }
}
