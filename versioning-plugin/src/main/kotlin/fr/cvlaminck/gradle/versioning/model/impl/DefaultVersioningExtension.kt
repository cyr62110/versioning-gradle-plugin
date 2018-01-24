package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.VersionTemplateContainer
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Action

open class DefaultVersioningExtension constructor(
        override val versions: VersionTemplateContainer
) : VersioningExtension {

    private var _defaultVersion: String? = null
    override val defaultVersion: String
        get() = _defaultVersion!!

    override fun defaultVersion(version: String) {
        _defaultVersion = version
    }

    override fun versions(configure: Action<in VersionTemplateContainer>) {
        configure.execute(versions)
    }
}
