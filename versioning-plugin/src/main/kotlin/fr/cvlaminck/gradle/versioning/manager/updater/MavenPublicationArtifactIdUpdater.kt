package fr.cvlaminck.gradle.versioning.manager.updater

import fr.cvlaminck.gradle.versioning.model.ArtifactIdTemplate
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.slf4j.LoggerFactory

class MavenPublicationArtifactIdUpdater : ArtifactIdUpdater {

    override fun updateArtifactId(project: Project, template: ArtifactIdTemplate, group: String?, name: String?, version: String?) {
        val publishingExtension = project.extensions.findByType(PublishingExtension::class.java)
        if (template.publicationNames.isEmpty() && publishingExtension.publications.size > 0) {
            log.debug("No publication specified in template '{}'. Publications will not be affected by versioning plugin.", template.name)
        }
        val publicationsToUpdate = publishingExtension.publications
                .filter { template.publicationNames.contains(it.name) }
                .filter { it is MavenPublication }
                .map { it as MavenPublication }
                .toList()
        if (publicationsToUpdate.size != template.publicationNames.size) {
            val missingPublicationNames = template.publicationNames
                    .filter { pubName -> publicationsToUpdate.none { it.name == pubName } }
            log.warn("Cannot find publication with names {}. Please check that names are matching.", missingPublicationNames)
        }
        publicationsToUpdate.forEach { updateMavenPublication(project, it, group, name, version) }
    }

    private fun updateMavenPublication(project: Project, publication: MavenPublication, group: String?, name: String?, version: String?) {
        val newGroupId = group ?: publication.groupId ?: project.group.toString()
        val newArtifactId = name ?: publication.artifactId ?: project.name.toString()
        val newVersion = version ?: publication.version ?: project.version.toString()

        log.debug("Updating publication '{}' with values: groupId={}, artifactId={}, version={}",
                publication.name, newGroupId, newArtifactId, newVersion)

        publication.groupId = newGroupId
        publication.artifactId = newArtifactId
        publication.version = newVersion
    }

    companion object {
        private val log = LoggerFactory.getLogger(MavenPublicationArtifactIdUpdater::class.java)
    }
}
