package com.example.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────
// RecipeDto.kt — DTO từ API
// ─────────────────────────────────────────────────────────────

data class RecipeDto(
    @SerializedName("id")           val id: Int,
    @SerializedName("title")        val title: String,
    @SerializedName("image_url")    val imageUrl: String,
    @SerializedName("author")       val author: AuthorDto,
    @SerializedName("cooking_time") val cookingTimeMinutes: Int,
    @SerializedName("rating")       val rating: Double,
    @SerializedName("category")     val category: String,
    @SerializedName("is_featured")  val isFeatured: Boolean = false
)

data class AuthorDto(
    @SerializedName("id")         val id: Int,
    @SerializedName("name")       val name: String,
    @SerializedName("avatar_url") val avatarUrl: String
)
