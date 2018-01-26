package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.VersionTemplate

open class DefaultVersionTemplate(
        private val _name: String
) : VersionTemplate {

    private var _template: String? = null

    private val _branchPatterns = mutableListOf<String>()

    override val template: String
        get() = _template!!

    override val branchPatterns: Collection<String>
        get() = _branchPatterns.toList()

    override fun getName(): String {
        return _name
    }

    override fun template(template: String) {
        _template = template
    }

    override fun branchPatterns(vararg branchPatterns: String) {
        _branchPatterns.addAll(branchPatterns)
    }

    override fun toString(): String {
        return template
    }
}
