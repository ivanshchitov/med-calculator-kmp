@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)

import java.security.MessageDigest
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.buildconfig)
}

buildConfig {
    className("AppConfig")
    useKotlinOutput()
    buildConfigField("VERSION_CODE", libs.versions.version.code.get().toLong())
    buildConfigField("VERSION_NAME", libs.versions.version.name.get())
}

kotlin {
    listOf(
        iosX64(),
        iosArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    androidLibrary {
       namespace = "org.dishch.medcalculator.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    applyDefaultHierarchyTemplate {
        common {
            group("nonWeb") {
                group("android") {
                    withAndroidTarget()
                }
                group("apple") {
                    withIos()
                }
            }
        }
    }

    sourceSets {
        getByName("androidMain").dependsOn(getByName("nonWebMain"))
        getByName("nonWebMain").dependencies {
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.preferences.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        wasmJsMain.dependencies {
            implementation(libs.androidx.datastore.core.web)
            implementation(libs.androidx.datastore.preferences.core.web)
            implementation(libs.androidx.datastore.core.okio.web)
            implementation(libs.kotlinx.browser)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)

    // Process specific target platforms
    add("kspAndroid", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)

}


@CacheableTask
abstract class GeneratePrecacheManifest : DefaultTask() {
    @get:Internal
    abstract val distributionDir: DirectoryProperty

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputFiles: ConfigurableFileCollection

    @get:OutputFile
    abstract val manifestFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val distDir = distributionDir.get().asFile
        val excluded = setOf("sw.js", "precache-manifest.json")
        val files = inputFiles.files
            .asSequence()
            .filter { it.isFile }
            .map { it.relativeTo(distDir).invariantSeparatorsPath }
            .filterNot { it in excluded }
            .filterNot { it.endsWith(".map") || it.endsWith(".LICENSE.txt") }
            .sorted()
            .toList()

        val digest = MessageDigest.getInstance("SHA-256")
        (files + "sw.js").forEach { relativePath ->
            val source = distDir.resolve(relativePath)
            require(source.isFile) {
                "Missing file for precache manifest: ${source.path}"
            }
            digest.update(relativePath.toByteArray())
            source.inputStream().use { input ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (true) {
                    val read = input.read(buffer)
                    if (read <= 0) break
                    digest.update(buffer, 0, read)
                }
            }
        }
        val version = digest.digest().joinToString("") { byte -> "%02x".format(byte) }.take(12)

        val json = buildString {
            append("{\"version\":\"")
            append(version)
            append("\",\"files\":[")
            files.forEachIndexed { index, file ->
                if (index > 0) append(',')
                append('"')
                append(file.replace("\\", "\\\\").replace("\"", "\\\""))
                append('"')
            }
            append("]}")
        }

        manifestFile.get().asFile.writeText(json)
    }
}

val wasmDistributionDir = layout.buildDirectory.dir("dist/wasmJs/productionExecutable")
val generatePrecacheManifest = tasks.register<GeneratePrecacheManifest>("generatePrecacheManifest") {
    distributionDir.set(wasmDistributionDir)
    inputFiles.from(
        fileTree(wasmDistributionDir) {
            exclude("precache-manifest.json")
        }
    )
    manifestFile.set(wasmDistributionDir.map { it.file("precache-manifest.json") })
}

tasks.matching { it.name == "wasmJsBrowserDistribution" }.configureEach {
    finalizedBy(generatePrecacheManifest)
}
