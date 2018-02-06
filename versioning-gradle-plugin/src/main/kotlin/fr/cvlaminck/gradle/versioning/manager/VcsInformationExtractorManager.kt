package fr.cvlaminck.gradle.versioning.manager

import fr.cvlaminck.gradle.versioning.exception.VersioningPluginException
import fr.cvlaminck.gradle.versioning.manager.vcs.GitInformationExtractor
import fr.cvlaminck.gradle.versioning.manager.vcs.VcsInformationExtractor
import fr.cvlaminck.gradle.versioning.model.VcsInformation
import org.gradle.api.Project
import java.nio.file.Paths
import javax.inject.Inject

class VcsInformationExtractorManager @Inject constructor(
        private val extractors: Collection<VcsInformationExtractor> = listOf(
                GitInformationExtractor()
        )
) {

    fun extractInformation(project: Project): VcsInformation {
        val path = project.projectDir.toPath()
        val extractor = extractors.firstOrNull { it.isRepository(path) } ?: throw VersioningPluginException("No vcs can be found in directory: $path")
        return extractor.extractInformation(path)
    }
}
