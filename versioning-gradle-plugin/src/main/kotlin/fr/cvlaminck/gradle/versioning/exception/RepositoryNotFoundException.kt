package fr.cvlaminck.gradle.versioning.exception

import java.nio.file.Path

class RepositoryNotFoundException(
        vcs: String,
        projectPath: Path
) : VersioningPluginException("Cannot find any $vcs repository in directory '$projectPath'")
