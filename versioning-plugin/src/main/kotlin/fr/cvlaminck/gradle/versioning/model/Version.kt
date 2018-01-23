package fr.cvlaminck.gradle.versioning.model

import org.gradle.api.Named

/**
 *
 */
interface Version: Named {

    /**
     * Regular expressions that should match the Git branch name to select this version.
     * A version is selected if at least one of the pattern matches the current Git branch name.
     */
    val branchPatterns: Array<String>

    /**
     * Pattern that will be used to generate the version if this [Version] is selected.
     */
    val versionTemplate: String
}
