// ─────────────────────────────────────────────────────────────
// build.gradle.kts (app module)
// Tất cả dependency cần thiết cho một Android project production
// ─────────────────────────────────────────────────────────────

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)           // Hilt DI
    alias(libs.plugins.ksp)            // KSP cho Room & Hilt
    kotlin("plugin.serialization")     // Kotlinx Serialization
}

android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        compose = true
        // viewBinding = true  ← KHÔNG cần khi dùng Jetpack Compose
        // Compose tự quản lý UI qua @Composable, không qua XML/ViewBinding
    }
}

dependencies {
    // ── Jetpack Compose BOM (quản lý version tất cả compose lib) ──
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)

    // ── ViewModel + Lifecycle ──────────────────────────────────────
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ── Hilt (Dependency Injection) ────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose) // hiltViewModel()

    // ── Retrofit + OkHttp (Network) ────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // ── Room (Local Database) ──────────────────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)           // coroutines support
    ksp(libs.androidx.room.compiler)

    // ── Kotlinx Coroutines ────────────────────────────────────────
    implementation(libs.kotlinx.coroutines.android)

    // ── Coil (Image Loading) ──────────────────────────────────────
    implementation(libs.coil.compose)

    // ── DataStore (thay thế SharedPreferences) ────────────────────
    implementation(libs.androidx.datastore.preferences)
}
