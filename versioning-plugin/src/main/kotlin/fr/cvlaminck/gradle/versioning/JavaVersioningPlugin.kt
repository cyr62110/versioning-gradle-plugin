package fr.cvlaminck.gradle.versioning

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.LoggerFactory

open class JavaVersioningPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        log.debug("Hello world")
    }

    companion object {
        private val log = LoggerFactory.getLogger(JavaVersioningPlugin::class.java)
    }
}
