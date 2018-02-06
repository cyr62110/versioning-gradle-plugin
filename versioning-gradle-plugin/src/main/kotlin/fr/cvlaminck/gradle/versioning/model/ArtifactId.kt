package fr.cvlaminck.gradle.versioning.model

data class ArtifactId(
        var group: String?,
        var name: String?,
        var version: String?
) {
    val empty: Boolean
        get() = group.isNullOrBlank() && name.isNullOrBlank() && version.isNullOrBlank()
}
