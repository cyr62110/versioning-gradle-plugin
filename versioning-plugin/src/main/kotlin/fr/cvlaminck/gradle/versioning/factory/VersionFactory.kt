package fr.cvlaminck.gradle.versioning.factory

import fr.cvlaminck.gradle.versioning.model.VersionTemplate
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersionTemplate
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator
import org.slf4j.LoggerFactory

open class VersionFactory(
        private val instantiator: Instantiator
) : NamedDomainObjectFactory<VersionTemplate> {

    override fun create(name: String): VersionTemplate {
        log.debug("Creating version '{}'.", name)
        return instantiator.newInstance(DefaultVersionTemplate::class.java, name)
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersionFactory::class.java)
    }
}
