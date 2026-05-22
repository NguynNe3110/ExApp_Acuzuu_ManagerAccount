package com.example.app.feature.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.example.app.core.result.Result
import com.example.app.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val logTag = "RegisterViewModel"

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _events = Channel<RegisterUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onRegisterClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            Log.d(logTag, "Register start for email=${uiState.value.email}")
            try {
                when (
                    val result = registerUseCase(
                        uiState.value.name,
                        uiState.value.email,
                        uiState.value.password,
                        uiState.value.confirmPassword
                    )
                ) {
                    is Result.Success -> {
                        Log.d(logTag, "Register success")
                        _events.send(RegisterUiEvent.NavigateToHome)
                    }
                    is Result.Error -> {
                        Log.w(logTag, "Register error: ${result.message}")
                        _events.send(RegisterUiEvent.ShowError(result.message))
                    }
                    is Result.Loading -> {
                        Log.d(logTag, "Register loading")
                    }
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
