package com.example.app.feature.auth.login

// ─────────────────────────────────────────────────────────────
// LoginUiState.kt — Trạng thái UI của màn hình Login
// ─────────────────────────────────────────────────────────────

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val emailError: String? = null,    // Lỗi validation field email
    val passwordError: String? = null  // Lỗi validation field password
) {
    // Form chỉ submit được khi email và password không trống
    val isSubmitEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
