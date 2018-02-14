package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.extension.TempDirectoryExtension
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@ExtendWith(TempDirectoryExtension::class)
internal class VersioningPluginMavenFunctionTest : VersioningPluginFunctionalTest() {

    @Test
    fun publishMavenMaster(
            @TempDirectoryExtension.Root projectDir: Path
    ) {
        Git.init()
                .setDirectory(projectDir.toFile())
                .call()

        prepareProject(projectDir, "mavenExample.gradle")

        val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withArguments("publish")
                .withPluginClasspath()
                .build()

        assertTrue(result.task(":updateArtifactId")!!.outcome == TaskOutcome.SUCCESS)
        assertTrue(result.task(":publish")!!.outcome == TaskOutcome.SUCCESS)

        checkPublication(
                projectDir.resolve(Paths.get("build", "repo")),
                projectDir.toFile().name,
                "1.0-RELEASE")
    }

    private fun checkPublication(repoPath: Path, artifactId: String, version: String) {
        val artifactPath = repoPath.resolve(Paths.get("fr", "cvlaminck", "gradle", artifactId))
        assertTrue(Files.exists(artifactPath))

        val metadataPath = artifactPath.resolve("maven-metadata.xml")
        assertTrue(Files.exists(metadataPath))
        assertTrue(Files.lines(metadataPath).anyMatch { it.contains("<release>$version</release>") })

        val versionPath = artifactPath.resolve(version)
        assertTrue(Files.exists(versionPath))

        val pomPath = versionPath.resolve("$artifactId-$version.pom")
        assertTrue(Files.lines(pomPath).anyMatch { it.contains("<version>$version</version>") })

        val artifactJarPath = versionPath.resolve("$artifactId-$version.jar")
        assertTrue(Files.exists(artifactJarPath))
    }
}
