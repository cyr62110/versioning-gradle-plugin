package fr.cvlaminck.gradle.versioning.extension

import org.junit.jupiter.api.extension.*
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class TempDirectoryExtension : AfterEachCallback, ParameterResolver {

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    @MustBeDocumented
    annotation class Root

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.getAnnotation(Root::class.java) != null && Path::class.java == parameterContext.parameter.type
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return getLocalStore(extensionContext).getOrComputeIfAbsent(KEY) { key -> createTempDirectory(extensionContext) }
    }

    override fun afterEach(context: ExtensionContext) {
        val tempDirectory = getLocalStore(context).get(KEY) as Path?
        if (tempDirectory != null) {
            delete(tempDirectory)
        }
    }

    private fun getLocalStore(context: ExtensionContext): ExtensionContext.Store {
        return context.getStore(localNamespace(context))
    }

    private fun localNamespace(context: ExtensionContext): ExtensionContext.Namespace {
        return ExtensionContext.Namespace.create(TempDirectoryExtension::class.java, context)
    }

    private fun createTempDirectory(context: ExtensionContext): Path {
        try {
            return Files.createTempDirectory(KEY)
        } catch (e: IOException) {
            throw ParameterResolutionException("Could not create temp directory", e)
        }

    }

    @Throws(IOException::class)
    private fun delete(tempDirectory: Path) {
        Files.walkFileTree(tempDirectory, object : SimpleFileVisitor<Path>() {

            @Throws(IOException::class)
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                return deleteAndContinue(file)
            }

            @Throws(IOException::class)
            override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                return deleteAndContinue(dir)
            }

            @Throws(IOException::class)
            private fun deleteAndContinue(path: Path): FileVisitResult {
                Files.delete(path)
                return FileVisitResult.CONTINUE
            }
        })
    }

    companion object {
        private val KEY = "tempDirectory"
    }
}
