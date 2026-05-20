package com.example.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────
// LoginResponseDto.kt — Data trả về khi đăng nhập thành công
// ─────────────────────────────────────────────────────────────

data class LoginResponseDto(
    @SerializedName("access_token")  val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user")          val user: UserDto
)

data class UserDto(
    @SerializedName("id")       val id: Int,
    @SerializedName("email")    val email: String,
    @SerializedName("name")     val name: String,
    @SerializedName("avatar")   val avatar: String?
)
