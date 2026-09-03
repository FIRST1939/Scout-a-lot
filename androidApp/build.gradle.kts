import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_21) } }

android {
    namespace = "com.frcteam1939.scouting2026.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.frcteam1939.scouting2026.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["appLabel"] = "FRC Scout Maker"
    }
    buildFeatures {
        compose = true
        // BuildConfig.DEBUG gates the offline demo-event seeding in AddEventScreen.
        buildConfig = true
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            manifestPlaceholders["appLabel"] = "FRC Scout Maker Dev"
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.room.runtime)
    implementation(libs.material)
    implementation(libs.compose.foundation)
    implementation("com.google.accompanist:accompanist-drawablepainter:0.37.3")
    implementation(libs.androidx.foundation)
    debugImplementation(libs.compose.ui.tooling)
}
