package com.example.app.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────
// Theme.kt — Material3 theme cho toàn app
// ─────────────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary         = Color(0xFF00BCD4),  // Cyan — màu chính của Acuzu
    onPrimary       = Color.White,
    primaryContainer = Color(0xFFE0F7FA),
    secondary       = Color(0xFF26C6DA),
    background      = Color(0xFFF5F5F5),
    surface         = Color.White,
    onSurface       = Color(0xFF212121)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
