package com.example.app.feature.auth.register

// ─────────────────────────────────────────────────────────────
// RegisterUiState.kt
// ─────────────────────────────────────────────────────────────

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
) {
    val isSubmitEnabled: Boolean
        get() = name.isNotBlank() && email.isNotBlank()
            && password.isNotBlank() && confirmPassword.isNotBlank()
            && !isLoading
}
