package com.example.app.domain.usecase

import com.example.app.core.result.Result
import com.example.app.domain.model.User
import com.example.app.domain.repository.AuthRepository
import javax.inject.Inject

// Validate + goi register
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<User> {
        if (name.isBlank()) return Result.Error("Ten khong duoc de trong")
        if (email.isBlank() || !email.contains("@")) return Result.Error("Email khong hop le")
        if (password.length < 6) return Result.Error("Mat khau phai co it nhat 6 ky tu")
        if (password != confirmPassword) return Result.Error("Mat khau khong khop")
        return repository.register(name.trim(), email.trim(), password)
    }
}
