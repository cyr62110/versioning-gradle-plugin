package fr.cvlaminck.gradle.versioning.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.LoggerFactory

open class JavaSetVersionTask : DefaultTask() {

    @TaskAction
    fun setVersion() {

    }

    companion object {
        private val log = LoggerFactory.getLogger(JavaSetVersionTask::class.java)
    }
}
