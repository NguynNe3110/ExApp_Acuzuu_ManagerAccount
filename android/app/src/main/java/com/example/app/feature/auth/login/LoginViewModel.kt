package com.example.app.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.app.core.result.Result
import com.example.app.domain.usecase.LoginUseCase
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// LoginViewModel.kt
// ─────────────────────────────────────────────────────────────

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val logTag = "LoginViewModel"

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = Channel<LoginUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            Log.d(logTag, "Login start for email=${uiState.value.email}")
            try {
                when (val result = loginUseCase(uiState.value.email, uiState.value.password)) {
                    is Result.Success -> {
                        Log.d(logTag, "Login success")
                        _events.send(LoginUiEvent.NavigateToHome)
                    }
                    is Result.Error -> {
                        Log.w(logTag, "Login error: ${result.message}")
                        _events.send(LoginUiEvent.ShowError(result.message))
                    }
                    is Result.Loading -> {
                        Log.d(logTag, "Login loading")
                    }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
