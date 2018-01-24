package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.VersionTemplate

open class DefaultVersionTemplate(
        private val _name: String
) : VersionTemplate {

    val _branchPatterns = mutableListOf<String>()

    

    override val branchPatterns: Collection<String>
        get() = _branchPatterns.toList()

    override fun getName(): String {
        return _name
    }
}
