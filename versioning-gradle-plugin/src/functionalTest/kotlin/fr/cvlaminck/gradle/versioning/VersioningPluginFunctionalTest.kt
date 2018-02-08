package fr.cvlaminck.gradle.versioning

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

internal open class VersioningPluginFunctionalTest {

    fun prepareProject(projectDir: Path, buildTemplate: String) {
        val srcDir = projectDir
                .resolve("src")
                .resolve("main")
                .resolve("java")
        Files.createDirectories(srcDir)

        val greetingClassIs = getResourceAsStream("Greetings.java")
        Files.copy(greetingClassIs, srcDir.resolve("Greetings.java"))
        greetingClassIs.close()

        val buildTemplateIs = getResourceAsStream(buildTemplate)
        Files.copy(buildTemplateIs, projectDir.resolve("build.gradle"))
    }

    private fun getResourceAsStream(resourceName: String): InputStream {
        return if (!resourceName.startsWith("/")) {
            this.javaClass.getResourceAsStream("/" + resourceName)
        } else {
            this.javaClass.getResourceAsStream(resourceName)
        }
    }
}
