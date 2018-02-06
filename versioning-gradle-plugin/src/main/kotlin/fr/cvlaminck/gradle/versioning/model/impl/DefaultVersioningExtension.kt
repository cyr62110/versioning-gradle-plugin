package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplateContainer
import fr.cvlaminck.gradle.versioning.model.VersioningExtension
import org.gradle.api.Action

open class DefaultVersioningExtension constructor(
        override val templateContainer: ArtifactIdTemplateContainer
) : VersioningExtension {

    override fun templates(configure: Action<in ArtifactIdTemplateContainer>) {
        configure.execute(templateContainer)
    }
}
