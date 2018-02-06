package fr.cvlaminck.gradle.versioning.factory

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.impl.DefaultArtifactIdTemplate
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator
import org.slf4j.LoggerFactory

open class VersionFactory(
        private val instantiator: Instantiator
) : NamedDomainObjectFactory<ArtifactIdTemplate> {

    override fun create(name: String): ArtifactIdTemplate {
        log.debug("Creating version '{}'.", name)
        return instantiator.newInstance(DefaultArtifactIdTemplate::class.java, name)
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersionFactory::class.java)
    }
}
