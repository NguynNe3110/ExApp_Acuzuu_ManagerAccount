package com.example.app.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
// Theme.kt — Màu lấy từ Figma design Acuzu
// ─────────────────────────────────────────────────────────────

// Primary colors
val BluePrimary    = Color(0xFF29B6F6)  // nút Login/Save/FAB
val BlueLight      = Color(0xFFE3F8FF)  // card background (Facebook tone)
val GreenLight     = Color(0xFFF5F8EE)  // card background (Google tone)
val RedPrimary     = Color(0xFFE53935)  // Edit profile button / log out
val TextBlack      = Color(0xFF1A1A1A)
val TextGray       = Color(0xFF9E9E9E)
val BorderGray     = Color(0xFFE0E0E0)
val BgWhite        = Color(0xFFFFFFFF)
val BgGray         = Color(0xFFF5F5F5)

// Password strength indicator colors (top→bottom = mức cao→thấp)
val StrengthGreen  = Color(0xFF4CAF50)
val StrengthYellow = Color(0xFFFFEB3B)
val StrengthOrange = Color(0xFFFF9800)
val StrengthRed    = Color(0xFFF44336)

private val LightColorScheme = lightColorScheme(
    primary          = BluePrimary,
    onPrimary        = BgWhite,
    secondary        = RedPrimary,
    background       = BgWhite,
    surface          = BgWhite,
    onBackground     = TextBlack,
    onSurface        = TextBlack,
    outline          = BorderGray,
    surfaceVariant   = BgGray
)

private val DarkColorScheme = darkColorScheme(
    primary          = BluePrimary,
    onPrimary        = BgWhite,
    secondary        = RedPrimary,
    background       = Color(0xFF121212),
    surface          = Color(0xFF1E1E1E),
    onBackground     = Color(0xFFE0E0E0),
    onSurface        = Color(0xFFE0E0E0),
    outline          = Color(0xFF424242),
    surfaceVariant   = Color(0xFF2C2C2C)
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextBlack),
    headlineMedium = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp),
    bodySmall = TextStyle(fontSize = 12.sp, color = TextGray),
    labelSmall = TextStyle(fontSize = 11.sp, color = TextGray)
)

private val AppShapes = Shapes(
    small  = RoundedCornerShape(8),
    medium = RoundedCornerShape(12),
    large  = RoundedCornerShape(20)
)

// Dark mode toggle — expose qua CompositionLocal
val LocalDarkMode = staticCompositionLocalOf { false }

@Composable
fun AppTheme(
    darkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalDarkMode provides darkMode) {
        MaterialTheme(
            colorScheme = if (darkMode) DarkColorScheme else LightColorScheme,
            typography  = AppTypography,
            shapes      = AppShapes,
            content     = content
        )
    }
}
