package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.VersionTemplate
import fr.cvlaminck.gradle.versioning.model.VersionTemplateContainer
import org.gradle.api.internal.DefaultPolymorphicDomainObjectContainer
import org.gradle.internal.reflect.Instantiator

open class DefaultVersionTemplateContainer(
        instantiator: Instantiator
) : DefaultPolymorphicDomainObjectContainer<VersionTemplate>(VersionTemplate::class.java, instantiator), VersionTemplateContainer
