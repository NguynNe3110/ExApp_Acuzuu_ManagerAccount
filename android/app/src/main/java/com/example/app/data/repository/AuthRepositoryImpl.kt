package com.example.app.data.repository

import com.example.app.core.result.Result
import com.example.app.data.mapper.toDomain
import com.example.app.data.remote.api.AuthApi
import com.example.app.data.remote.dto.request.LoginRequestDto
import com.example.app.data.session.SessionManager
import com.example.app.domain.model.User
import com.example.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// AuthRepositoryImpl.kt — Xử lý logic đăng nhập/đăng xuất
// ─────────────────────────────────────────────────────────────

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))

            if (response.success && response.data != null) {
                // Lưu token để dùng cho các request tiếp theo
                sessionManager.saveTokens(
                    accessToken  = response.data.accessToken,
                    refreshToken = response.data.refreshToken
                )
                Result.Success(response.data.user.toDomain())
            } else {
                Result.Error(response.message)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Đăng nhập thất bại")
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            sessionManager.clearSession()
            Result.Success(Unit)
        } catch (e: Exception) {
            // Dù API logout lỗi vẫn xóa session local
            sessionManager.clearSession()
            Result.Success(Unit)
        }
    }

    override val isLoggedIn: Flow<Boolean> = sessionManager.isLoggedInFlow
}
