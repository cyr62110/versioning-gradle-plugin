package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.extension.TempDirectoryExtension
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path

@ExtendWith(TempDirectoryExtension::class)
internal class VersioningPluginSimpleJavaFunctionalTest : VersioningPluginFunctionalTest() {

    @Test
    fun simpleJava(
            @TempDirectoryExtension.Root projectDir: Path
    ) {
        println(projectDir)
        prepareProject(projectDir, "simpleHelloWorld.gradle")

        val result = GradleRunner.create()
                .withProjectDir(projectDir.toFile())
                .withArguments("assemble")
                // FIXME .withPluginClasspath()
                .build()

        assertTrue(result.task(":assemble").outcome == TaskOutcome.SUCCESS)
    }
}
