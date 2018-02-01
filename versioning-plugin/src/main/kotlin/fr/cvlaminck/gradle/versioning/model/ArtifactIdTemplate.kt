package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Named

/**
 * Template used to generate the artifact id of an application/library.
 *
 * This template will be used if the name of the current Git branch matches one of the pattern listed in [branchPatterns].
 * If multiple templates are applicable, the first one registered will be used.
 */
interface ArtifactIdTemplate : Named {

    /**
     * Regular expressions that should match the Git branch name to select this template.
     * A version is selected if at least one of the pattern matches the current Git branch name.
     */
    val branchPatterns: Collection<String>

    /**
     * Collection of name of publications that we must update the version using this template.
     */
    val publicationNames: Collection<String>

    /**
     * Template used to generate the version name.
     */
    val template: String

    fun template(template: String)

    fun publications(vararg publicationNames: String)

    fun branchPatterns(vararg branchPatterns: String)
}
