import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.gradle.jvm.tasks.Jar
import java.io.File

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "io.github.opletter.css2kobweb"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = "io.github.opletter.css2kobweb.MainKt"
        }
        compilations.getByName("main") {
            dependencies {
                implementation("com.fleeksoft.ksoup:ksoup:0.2.5")
            }
        }
    }
    js {
        browser()
    }
}

// Create fat JAR task
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Create a fat JAR with all dependencies"

    archiveClassifier.set("")
    archiveBaseName.set("css2kobweb")
    archiveVersion.set(project.version.toString())

    // Set the main class
    manifest {
        attributes("Main-Class" to "io.github.opletter.css2kobweb.MainKt")
    }

    // Include compiled classes
    from(project.tasks.named("compileKotlinJvm").get().outputs)

    // Include all dependencies - use the jvm target's runtime classpath
    val jvmCompilation = kotlin.targets.getByName("jvm").compilations.getByName("main")
    val runtimeClasspath = jvmCompilation.runtimeDependencyFiles!!
    runtimeClasspath.forEach { file: File ->
        if (file.name.endsWith("jar")) {
            from(zipTree(file))
        }
    }

    // Exclude signature files to avoid conflicts
    exclude("META-INF/*.SF")
    exclude("META-INF/*.DSA")
    exclude("META-INF/*.RSA")

    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
}