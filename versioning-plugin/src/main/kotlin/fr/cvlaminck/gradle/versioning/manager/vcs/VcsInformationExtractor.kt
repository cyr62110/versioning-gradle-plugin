package fr.cvlaminck.gradle.versioning.manager.vcs

import fr.cvlaminck.gradle.versioning.model.VcsInformation
import org.gradle.api.Project
import java.nio.file.Path

interface VcsInformationExtractor {

    /**
     * Name of the vcs from which we will extract information
     */
    val name: String

    /**
     * Returns true if [path] points to a repository supported by this extractor.
     *
     * @return true if [path] points to a supported repository, false otherwise.
     */
    fun isRepository(path: Path): Boolean

    /**
     * Extract the information from the repository.
     */
    fun extractInformation(path: Path): VcsInformation
}
