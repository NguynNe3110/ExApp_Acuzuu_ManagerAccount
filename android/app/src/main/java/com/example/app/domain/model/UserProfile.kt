package com.example.app.domain.model

// ─────────────────────────────────────────────────────────────
// UserProfile.kt — Hồ sơ người dùng
// ─────────────────────────────────────────────────────────────

data class UserProfile(
    val id: Int,
    val username: String,          // "Uzuu of kings"
    val avatarUrl: String?,
    val maxPasswordLevel: PasswordLevel = PasswordLevel.LEVEL_3
)
