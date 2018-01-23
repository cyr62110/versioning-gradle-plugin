package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.Version

open class DefaultVersion(
        private val _name: String
) : Version {

    override fun getName(): String {
        return _name
    }
}
