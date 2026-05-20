package com.example.app.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
// Theme.kt — Màu sắc lấy trực tiếp từ Figma design
// Background: kem vàng ấm #FAF8EF
// Card: #F5F0E4
// Primary: đen #1A1A1A
// ─────────────────────────────────────────────────────────────

// Màu chính
val Cream       = Color(0xFFFAF8EF)   // background chính
val CreamCard   = Color(0xFFF5F0E4)   // card item
val CreamDark   = Color(0xFFEDE8D5)   // border / divider
val TextPrimary = Color(0xFF1A1A1A)   // tiêu đề lớn
val TextSecondary = Color(0xFF6B6B6B) // text phụ
val StarYellow  = Color(0xFFFFB800)   // rating star
val White       = Color(0xFFFFFFFF)
val NavCircle   = Color(0xFFEDE8D5)   // vòng tròn active nav

private val AppColorScheme = lightColorScheme(
    primary          = TextPrimary,
    onPrimary        = White,
    primaryContainer = CreamCard,
    background       = Cream,
    surface          = White,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    outline          = CreamDark,
    surfaceVariant   = CreamCard
)

private val AppTypography = Typography(
    // "Hi Chef" heading
    displayLarge = TextStyle(
        fontSize   = 36.sp,
        fontWeight = FontWeight.Bold,
        color      = TextPrimary
    ),
    // Section headings
    titleLarge = TextStyle(
        fontSize   = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color      = TextPrimary
    ),
    // Recipe card title
    titleMedium = TextStyle(
        fontSize   = 16.sp,
        fontWeight = FontWeight.Medium,
        color      = TextPrimary
    ),
    // Social card title
    headlineMedium = TextStyle(
        fontSize   = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color      = TextPrimary
    ),
    bodySmall = TextStyle(
        fontSize = 11.sp,
        color    = TextSecondary
    )
)

private val AppShapes = Shapes(
    small  = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large  = RoundedCornerShape(24.dp)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}
