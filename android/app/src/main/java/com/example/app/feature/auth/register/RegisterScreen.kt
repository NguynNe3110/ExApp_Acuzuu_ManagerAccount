package com.example.app.feature.auth.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.core.ui.BluePrimary
import com.example.app.core.ui.BlueLight
import com.example.app.core.ui.GreenLight

// ─────────────────────────────────────────────────────────────
// RegisterScreen.kt — Màn hình đăng ký
// Design: giống Login nhưng tiêu đề "Register", không có
//         "New user?" text, nút "Register"
// ─────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterUiEvent.NavigateToHome -> onNavigateToHome()
                is RegisterUiEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                placeholder = { Text("Name", color = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = { Text("Email", color = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                placeholder = { Text("Password", color = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                placeholder = { Text("Confirm password", color = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            )
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = viewModel::onRegisterClick,
                enabled = uiState.isSubmitEnabled,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Register", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider "or"
            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
                Text("  or  ", color = MaterialTheme.colorScheme.outline, style = MaterialTheme.typography.bodySmall)
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
            }

            Spacer(Modifier.height(16.dp))

            // Continue with Google
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = GreenLight),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        Text("G", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Continue with Google", color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(Modifier.height(12.dp))

            // Continue with Facebook
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BlueLight),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                        Text("f", color = Color(0xFF1877F2), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Continue with Facebook", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
