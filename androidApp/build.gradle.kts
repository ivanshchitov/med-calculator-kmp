import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

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
    }
    signingConfigs {
        create("release") {
            val properties = Properties().apply {
                val propertiesFile = rootProject.file("local.properties")
                if (propertiesFile.exists()) {
                    load(FileInputStream(propertiesFile))
                }
            }

            // Reference variables from local.properties
            storeFile = file(properties.getProperty("RELEASE_STORE_FILE") ?: "")
            storePassword = properties.getProperty("RELEASE_STORE_PASSWORD") ?: ""
            keyAlias = properties.getProperty("RELEASE_KEY_ALIAS") ?: ""
            keyPassword = properties.getProperty("RELEASE_KEY_PASSWORD") ?: ""
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
}
