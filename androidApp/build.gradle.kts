import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

// providers.fileContents keeps the value configuration-cache aware, unlike reading the file directly.
val localProperties = providers.fileContents(
    rootProject.layout.projectDirectory.file("local.properties")
).asText

fun localProperty(name: String): String? =
    localProperties.orNull
        ?.lineSequence()
        ?.map(String::trim)
        ?.firstOrNull { it.startsWith("$name=") }
        ?.substringAfter('=')
        ?.trim()

val myTrackerSdkKey: String = providers.gradleProperty("MYTRACKER_SDK_KEY").orNull
    ?: providers.environmentVariable("MYTRACKER_SDK_KEY").orNull
    ?: localProperty("MYTRACKER_SDK_KEY")
    ?: ""

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.koin.android)
    implementation(libs.mytracker.sdk)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "org.dishch.medcalculator"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.dishch.medcalculator"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()
        buildConfigField("String", "MYTRACKER_SDK_KEY", "\"$myTrackerSdkKey\"")
    }
    signingConfigs {
        create("release") {
            // Reference variables from local.properties
            localProperty("RELEASE_STORE_FILE")?.takeIf(String::isNotEmpty)?.let { storeFilePath ->
                storeFile = file(storeFilePath)
                storePassword = localProperty("RELEASE_STORE_PASSWORD")
                keyAlias = localProperty("RELEASE_KEY_ALIAS")
                keyPassword = localProperty("RELEASE_KEY_PASSWORD")
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

if (myTrackerSdkKey.isBlank()) {
    logger.warn(
        "MYTRACKER_SDK_KEY is not set — MyTracker analytics is disabled. " +
            "Set it in local.properties or via the MYTRACKER_SDK_KEY environment variable."
    )
}
