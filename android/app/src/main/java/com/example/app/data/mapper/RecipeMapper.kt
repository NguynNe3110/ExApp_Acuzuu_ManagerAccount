package com.example.app.data.mapper

import com.example.app.data.remote.dto.response.AuthorDto
import com.example.app.data.remote.dto.response.RecipeDto
import com.example.app.domain.model.Author
import com.example.app.domain.model.Recipe

// ─────────────────────────────────────────────────────────────
// RecipeMapper.kt — DTO → Domain Model
// ─────────────────────────────────────────────────────────────

fun RecipeDto.toDomain(): Recipe = Recipe(
    id                  = id,
    title               = title,
    imageUrl            = imageUrl,
    author              = author.toDomain(),
    cookingTimeMinutes  = cookingTimeMinutes,
    rating              = rating,
    category            = category,
    isFeatured          = isFeatured
)

fun AuthorDto.toDomain(): Author = Author(
    id        = id,
    name      = name,
    avatarUrl = avatarUrl
)

fun List<RecipeDto>.toDomain(): List<Recipe> = map { it.toDomain() }
