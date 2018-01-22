package fr.cvlaminck.gradle.versioning.exception

import org.gradle.api.GradleException

class VersioningPluginException(
        message: String
) : GradleException(message)
