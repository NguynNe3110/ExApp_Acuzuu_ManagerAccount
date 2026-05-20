package com.example.app.domain.model

// ─────────────────────────────────────────────────────────────
// User.kt — Domain Model cho người dùng
// ─────────────────────────────────────────────────────────────

data class User(
    val id: Int,
    val email: String,
    val name: String,
    val avatarUrl: String?
)
