package fr.cvlaminck.gradle.versioning.manager.vcs

import fr.cvlaminck.gradle.versioning.model.VcsInformation
import java.nio.file.Path

class GitInformationExtractor : VcsInformationExtractor {

    override fun isRepository(path: Path): Boolean {
        TODO("Not implemented")
    }

    override fun extractInformation(path: Path): VcsInformation {
        TODO("Not implemented")
    }
}
