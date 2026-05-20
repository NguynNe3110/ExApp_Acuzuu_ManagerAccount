package com.example.app.data.remote.dto.request

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────
// LoginRequestDto.kt — Body gửi lên server khi đăng nhập
// ─────────────────────────────────────────────────────────────

data class LoginRequestDto(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)
