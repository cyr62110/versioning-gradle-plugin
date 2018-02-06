package fr.cvlaminck.gradle.versioning.model.impl

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplateContainer
import org.gradle.api.internal.DefaultPolymorphicDomainObjectContainer
import org.gradle.internal.reflect.Instantiator

open class DefaultArtifactIdTemplateContainer(
        instantiator: Instantiator
) : DefaultPolymorphicDomainObjectContainer<ArtifactIdTemplate>(ArtifactIdTemplate::class.java, instantiator), ArtifactIdTemplateContainer
