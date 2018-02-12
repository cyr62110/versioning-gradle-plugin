package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.extension.TempDirectoryExtension
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@ExtendWith(TempDirectoryExtension::class)
internal class VersioningPluginJavaFunctionalTest : VersioningPluginFunctionalTest() {

    @Test
    fun assembleMaster(
            @TempDirectoryExtension.Root projectDir: Path
    ) {
        Git.init()
                .setDirectory(projectDir.toFile())
                .call()

        prepareProject(projectDir, "javaExample.gradle")

        val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()

        val libsPath = projectDir.resolve(Paths.get("build", "libs", "${projectDir.fileName}-1.0-RELEASE.jar"))

        assertTrue(result.task(":updateArtifactId").outcome == TaskOutcome.SUCCESS)
        assertTrue(result.task(":assemble").outcome == TaskOutcome.SUCCESS)
        assertTrue(Files.exists(libsPath))
    }

    @Test
    fun assembleDevelop(
            @TempDirectoryExtension.Root projectDir: Path
    ) {
        val git = Git.init()
                .setDirectory(projectDir.toFile())
                .call()
        git.commit()
                .setMessage("initial commit")
                .call()
        git.branchRename()
                .setNewName("develop")
                .call()

        prepareProject(projectDir, "javaExample.gradle")

        val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()

        val libsPath = projectDir.resolve(Paths.get("build", "libs", "${projectDir.fileName}-1.0-SNAPSHOT.jar"))

        assertTrue(result.task(":updateArtifactId").outcome == TaskOutcome.SUCCESS)
        assertTrue(result.task(":assemble").outcome == TaskOutcome.SUCCESS)
        assertTrue(Files.exists(libsPath))
    }
}
