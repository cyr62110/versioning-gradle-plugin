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
     * Regular expressions that should match the branch name to select this version.
     * A version is selected if at least one of the pattern matches the current Git branch name.
     */
    val branchPatterns: Collection<String>

    /**
     * Collection of name of publications that we must update the version using this version.
     * If empty, the version will be applied to all publications.
     */
    val publicationNames: Collection<String>

    /**
     * Template used to generate the version name.
     */
    val version: String

    /**
     * Set the template used to generate the version name.
     */
    fun version(template: String)

    /**
     * Restrict this version to be applied to publications with name included [publicationNames].
     */
    fun publications(vararg publicationNames: String)

    /**
     * Add a branch pattern.
     */
    fun branch(branchPattern: String)

    /**
     * Add branch patterns.
     */
    fun branches(vararg branchPatterns: String)
}
