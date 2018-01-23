package fr.cvlaminck.gradle.versioning.factory

import fr.cvlaminck.gradle.versioning.JavaVersioningPlugin
import fr.cvlaminck.gradle.versioning.model.Version
import fr.cvlaminck.gradle.versioning.model.impl.DefaultVersion
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator
import org.slf4j.LoggerFactory

open class VersionFactory(
        private val instantiator: Instantiator
) : NamedDomainObjectFactory<Version> {

    override fun create(name: String): Version {
        log.debug("Creating version '{}'.", name)
        return instantiator.newInstance(DefaultVersion::class.java, name)
    }

    companion object {
        private val log = LoggerFactory.getLogger(VersionFactory::class.java)
    }
}
