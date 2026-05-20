package com.example.app.feature.auth.register

sealed class RegisterUiEvent {
    data object NavigateToHome : RegisterUiEvent()
    data class ShowError(val message: String) : RegisterUiEvent()
}
