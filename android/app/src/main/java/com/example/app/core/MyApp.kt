package com.example.app.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// ─────────────────────────────────────────────────────────────
// MyApp.kt — Application class, điểm khởi đầu của Hilt
//
// @HiltAndroidApp: annotation này bắt buộc để Hilt hoạt động
//   → Hilt generate code tự động cho dependency injection
//   → Phải khai báo trong AndroidManifest.xml:
//     android:name=".core.MyApp"
// ─────────────────────────────────────────────────────────────

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Khởi tạo các library cần Application context ở đây
        // Ví dụ: Timber.plant(Timber.DebugTree())
    }
}
