package fr.cvlaminck.gradle.versioning.model

/**
 * Information that can be extracted from the vcs used in the project on which
 * this plugin is applied.
 */
data class VcsInformation(
        /**
         * Name of the vcs from which we have extracted the information
         */
        val vcs: String,
        /**
         * Name of the current branch.
         */
        val branchName: String,
        /**
         * Tags or labels.
         */
        val tags: Collection<String>
)
