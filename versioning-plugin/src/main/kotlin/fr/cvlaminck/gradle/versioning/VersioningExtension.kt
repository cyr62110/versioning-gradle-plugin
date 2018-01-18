package fr.cvlaminck.gradle.versioning

import org.gradle.api.Project

class VersioningExtension {

    private var version: String? = null

    fun version(version: String) {
        this.version = version
    }

    companion object {
        private val NAME = "versioning"
        fun create(project: Project): VersioningExtension = project.extensions.create(NAME, VersioningExtension::class.java)
    }
}
