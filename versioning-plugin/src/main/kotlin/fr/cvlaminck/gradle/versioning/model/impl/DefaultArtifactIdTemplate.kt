package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate

open class DefaultArtifactIdTemplate(
        private val _name: String
) : ArtifactIdTemplate {

    private var _template: String? = null
    private val _branchPatterns = mutableListOf<String>()
    private val _publicationNames = mutableListOf<String>()

    override val branchPatterns: Collection<String>
        get() = _branchPatterns.toList()

    override val template: String
        get() = _template!!

    override val publicationNames: Collection<String>
        get() = _publicationNames.toList()

    override fun getName(): String {
        return _name
    }

    override fun branchPatterns(vararg branchPatterns: String) {
        _branchPatterns.addAll(branchPatterns)
    }

    override fun template(template: String) {
        _template = template
    }

    override fun publications(vararg publicationNames: String) {
        _publicationNames.addAll(publicationNames)
    }

    override fun toString(): String {
        return "DefaultArtifactIdTemplate(" +
                "name=$name, " +
                "template=$_template, " +
                "branchPatterns=$_branchPatterns, " +
                "publicationNames=$_publicationNames" +
                ")"
    }
}
