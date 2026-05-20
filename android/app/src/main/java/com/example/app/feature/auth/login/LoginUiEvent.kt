package com.example.app.feature.auth.login

// ─────────────────────────────────────────────────────────────
// LoginUiEvent.kt — Sự kiện 1 lần từ LoginViewModel → UI
// ─────────────────────────────────────────────────────────────

sealed class LoginUiEvent {
    data object NavigateToHome : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}
