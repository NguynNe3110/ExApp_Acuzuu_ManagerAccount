package com.example.app.domain.usecase

import com.example.app.core.result.Result
import com.example.app.domain.model.User
import com.example.app.domain.repository.AuthRepository
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// LoginUseCase.kt — Validate + gọi login
// ─────────────────────────────────────────────────────────────

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Validate trước khi gọi API — business rule
        if (email.isBlank() || !email.contains("@")) {
            return Result.Error("Email không hợp lệ")
        }
        if (password.length < 6) {
            return Result.Error("Mật khẩu phải có ít nhất 6 ký tự")
        }
        return repository.login(email.trim(), password)
    }
}
