package com.example.app.core.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.example.app.core.result.Result

// ─────────────────────────────────────────────────────────────
// FlowExtensions.kt — Các extension tiện ích cho Flow
// ─────────────────────────────────────────────────────────────

/**
 * Bọc Flow thành Flow<Result<T>>
 * Tự động catch exception và wrap thành Result.Error
 *
 * Ví dụ dùng:
 *   repository.getUsers()          // Flow<List<User>>
 *       .asResult()                // Flow<Result<List<User>>>
 *       .collect { result -> ... }
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .catch { e -> emit(Result.Error(e.message ?: "Unknown error")) }
