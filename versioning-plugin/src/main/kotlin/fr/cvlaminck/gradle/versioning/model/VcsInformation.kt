package fr.cvlaminck.gradle.versioning.model

/**
 * Information that can be extracted from the vcs used in the project on which
 * this plugin is applied.
 */
interface VcsInformation {

    /**
     * Name of the current branch.
     */
    val branchName: String
}
