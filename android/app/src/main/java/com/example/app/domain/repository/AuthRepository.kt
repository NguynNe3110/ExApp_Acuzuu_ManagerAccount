package com.example.app.domain.repository

import com.example.app.core.result.Result
import com.example.app.domain.model.User
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────
// AuthRepository.kt — Interface cho Authentication
// ─────────────────────────────────────────────────────────────

interface AuthRepository {

    /** Đăng nhập, lưu token vào DataStore */
    suspend fun login(email: String, password: String): Result<User>

    /** Đăng xuất, xóa token */
    suspend fun logout(): Result<Unit>

    /** Flow để observe trạng thái đăng nhập */
    val isLoggedIn: Flow<Boolean>
}
