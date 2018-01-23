package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.Version
import fr.cvlaminck.gradle.versioning.model.VersionContainer
import org.gradle.api.internal.DefaultPolymorphicDomainObjectContainer
import org.gradle.internal.reflect.Instantiator

open class DefaultVersionContainer(
        instantiator: Instantiator
) : DefaultPolymorphicDomainObjectContainer<Version>(Version::class.java, instantiator), VersionContainer
