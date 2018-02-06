package fr.cvlaminck.gradle.versioning.exception

import org.gradle.api.GradleException

open class VersioningPluginException(
        message: String,
        throwable: Throwable? = null
) : GradleException(message, throwable)
