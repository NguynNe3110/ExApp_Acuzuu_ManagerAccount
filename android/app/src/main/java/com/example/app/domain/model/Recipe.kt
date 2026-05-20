package com.example.app.domain.model

// ─────────────────────────────────────────────────────────────
// Recipe.kt — Domain model cho công thức nấu ăn
// Dựa theo Figma design: image, title, author, time, rating
// ─────────────────────────────────────────────────────────────

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val author: Author,
    val cookingTimeMinutes: Int,
    val rating: Double,
    val category: String,
    val isFeatured: Boolean = false
)

data class Author(
    val id: Int,
    val name: String,
    val avatarUrl: String
)
