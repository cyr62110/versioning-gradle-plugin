package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.extension.TempDirectoryExtension
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions
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
                .withArguments("publish", "--debug")
                .withPluginClasspath()
                .build()

        val artifactPath = projectDir.resolve(Paths.get("build", "repo", "fr", "cvlaminck", "gradle", "1.0-RELEASE"))

        Assertions.assertTrue(result.task(":updateArtifactId")!!.outcome == TaskOutcome.SUCCESS)
        Assertions.assertTrue(result.task(":publish")!!.outcome == TaskOutcome.SUCCESS)
        Assertions.assertTrue(Files.exists(artifactPath))
    }
}
