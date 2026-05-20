package com.example.app.data.remote

import com.example.app.data.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// AuthInterceptor.kt — Tự động đính kèm token vào mọi request
//
// Interceptor chạy TRƯỚC khi request được gửi đi.
// Không cần thêm Authorization header thủ công ở từng API call.
// ─────────────────────────────────────────────────────────────

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Lấy token từ DataStore (SessionManager)
        val token = sessionManager.getAccessTokenBlocking()

        // Nếu không có token (chưa đăng nhập), gửi request bình thường
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        // Thêm header Authorization: Bearer <token>
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
