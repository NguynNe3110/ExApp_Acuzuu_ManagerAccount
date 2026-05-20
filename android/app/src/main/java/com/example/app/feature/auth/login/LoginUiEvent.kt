package com.example.app.feature.auth.login

sealed class LoginUiEvent {
    data object NavigateToHome : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
}
