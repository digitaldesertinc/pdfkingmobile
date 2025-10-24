plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.pdfking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pdfking"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.activity:activity-compose:1.8.0")

    // PDF viewing (Android PDF Viewer)
    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    // PDFBox for later PDF modifications (pdfbox-android)
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
}
