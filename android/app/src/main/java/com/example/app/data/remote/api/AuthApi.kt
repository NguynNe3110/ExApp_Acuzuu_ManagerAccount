package com.example.app.data.remote.api

import com.example.app.data.remote.dto.BaseResponseDto
import com.example.app.data.remote.dto.request.LoginRequestDto
import com.example.app.data.remote.dto.response.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

// ─────────────────────────────────────────────────────────────
// AuthApi.kt — Các endpoint liên quan đến Authentication
// ─────────────────────────────────────────────────────────────

interface AuthApi {

    /** POST /api/v1/auth/login */
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): BaseResponseDto<LoginResponseDto>

    /** POST /api/v1/auth/logout */
    @POST("api/v1/auth/logout")
    suspend fun logout(): BaseResponseDto<Unit>
}
