package fr.cvlaminck.gradle.versioning

import fr.cvlaminck.gradle.versioning.extension.TempDirectoryExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.file.Path

@ExtendWith(TempDirectoryExtension::class)
class VersioningPluginSimpleJavaFunctionalTest {

    @TempDirectoryExtension.Root
    private lateinit var buildDir: Path

    @Test
    fun simpleJava() {
        println(buildDir)
    }
}
