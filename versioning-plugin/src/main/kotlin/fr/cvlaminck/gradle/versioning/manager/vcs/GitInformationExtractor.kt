package fr.cvlaminck.gradle.versioning.manager.vcs

import fr.cvlaminck.gradle.versioning.exception.RepositoryNotFoundException
import fr.cvlaminck.gradle.versioning.model.VcsInformation
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.lib.RepositoryBuilder
import org.gradle.api.Project
import java.nio.file.Path

class GitInformationExtractor : VcsInformationExtractor {

    override val name: String = "git"

    override fun isRepository(path: Path): Boolean {
        try {
            openRepository(path)
            return true
        } catch (e: RepositoryNotFoundException) {
            return false
        }
    }

    override fun extractInformation(path: Path): VcsInformation {
        val repository = openRepository(path)
        return VcsInformation(
                name,
                repository.branch,
                setOf() // FIXME Implements
        )
    }

    @Throws(RepositoryNotFoundException::class)
    private fun openRepository(path: Path): Repository {
        val builder = RepositoryBuilder()
        builder.findGitDir(path.toFile())
        if (builder.gitDir == null) {
            throw RepositoryNotFoundException(name, path)
        }
        return builder.build()
    }
}
