package com.example.app.domain.repository

import com.example.app.core.result.Result
import com.example.app.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────
// RecipeRepository.kt — Interface cho Recipe data
// ─────────────────────────────────────────────────────────────

interface RecipeRepository {
    suspend fun getSuggestedRecipes(): Result<List<Recipe>>
    suspend fun getFriendsRecipes(): Result<List<Recipe>>
    fun searchRecipes(query: String): Flow<Result<List<Recipe>>>
}
