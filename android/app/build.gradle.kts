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
    alias(libs.plugins.kotlin.serialization)     // Kotlinx Serialization
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
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
        buildConfig = true
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
    implementation("androidx.compose.material:material-icons-extended")
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

    // ── Material Components (XML themes) ───────────────────────────
    implementation("com.google.android.material:material:1.12.0")

    // ── DataStore (thay thế SharedPreferences) ────────────────────
    implementation(libs.androidx.datastore.preferences)
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.13.0"))

    // Firebase Auth + Firestore (BoM quản lý version)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics")

    // Coroutines Task.await()
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

}
