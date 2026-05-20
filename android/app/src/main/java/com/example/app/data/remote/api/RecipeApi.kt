package com.example.app.data.remote.api

import com.example.app.data.remote.dto.BaseResponseDto
import com.example.app.data.remote.dto.response.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Query

// ─────────────────────────────────────────────────────────────
// RecipeApi.kt — Endpoints cho Recipe
// ─────────────────────────────────────────────────────────────

interface RecipeApi {

    /** GET /api/v1/recipes/suggested — "New category you'll love" */
    @GET("api/v1/recipes/suggested")
    suspend fun getSuggestedRecipes(): BaseResponseDto<List<RecipeDto>>

    /** GET /api/v1/recipes/friends — "Your friends are cooking" */
    @GET("api/v1/recipes/friends")
    suspend fun getFriendsRecipes(): BaseResponseDto<List<RecipeDto>>

    /** GET /api/v1/recipes/search?q=... */
    @GET("api/v1/recipes/search")
    suspend fun searchRecipes(@Query("q") query: String): BaseResponseDto<List<RecipeDto>>
}
