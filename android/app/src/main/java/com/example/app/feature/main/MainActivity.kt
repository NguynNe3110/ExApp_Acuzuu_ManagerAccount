package com.example.app.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app.core.ui.AppTheme
import com.example.app.feature.main.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

// ─────────────────────────────────────────────────────────────
// MainActivity.kt — Entry point duy nhất của app (Single Activity)
//
// @AndroidEntryPoint: cho phép Hilt inject vào Activity này
//
// Single Activity Pattern với Compose:
//   → Chỉ có 1 Activity duy nhất
//   → Mọi "màn hình" đều là Composable function
//   → Navigation dùng NavController thay vì startActivity()
//   → Không cần Fragment!
//
// Tại sao không dùng viewBinding ở đây?
//   → setContent { } thay thế hoàn toàn setContentView(R.layout.xxx)
//   → Không có XML layout → không có gì để bind
// ─────────────────────────────────────────────────────────────

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hiển thị content full màn hình (status bar + navigation bar)
        enableEdgeToEdge()

        // setContent thay thế cho setContentView() + viewBinding
        setContent {
            AppTheme {
                AppNavGraph()
            }
        }
    }
}
