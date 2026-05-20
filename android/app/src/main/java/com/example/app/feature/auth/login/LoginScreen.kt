package com.example.app.feature.auth.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.core.ui.BluePrimary
import com.example.app.core.ui.BlueLight
import com.example.app.core.ui.GreenLight

// ─────────────────────────────────────────────────────────────
// LoginScreen.kt — Màn hình đăng nhập
// Design: nền trắng, tiêu đề "Login" lớn, 2 field, nút xanh,
//         "or" divider, 2 social buttons bo tròn
// ─────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.NavigateToHome -> onNavigateToHome()
                is LoginUiEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            // Tiêu đề "Login"
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            // Username field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                placeholder = { Text("Username", color = MaterialTheme.colorScheme.outline) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp) // Figma: viền vuông
            )
            Spacer(Modifier.height(16.dp))

            // Password field
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
            Spacer(Modifier.height(8.dp))

            // "New user? Create an account"
            Text(
                text = buildAnnotatedString {
                    append("New user? ")
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Create an account")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(onClick = onNavigateToRegister)
            )
            Spacer(Modifier.height(24.dp))

            // Nút Login xanh to
            Button(
                onClick = viewModel::onLoginClick,
                enabled = uiState.isSubmitEnabled,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Login", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(24.dp))

            // "— or —" divider
            OrDivider()

            Spacer(Modifier.height(16.dp))

            // Continue with Google
            SocialButton(
                text = "Continue with Google",
                backgroundColor = GreenLight,
                iconUrl = "https://logo.clearbit.com/google.com",
                onClick = { /* Google OAuth */ }
            )
            Spacer(Modifier.height(12.dp))

            // Continue with Facebook
            SocialButton(
                text = "Continue with Facebook",
                backgroundColor = BlueLight,
                iconUrl = "https://logo.clearbit.com/facebook.com",
                onClick = { /* Facebook OAuth */ }
            )
        }
    }
}

@Composable
private fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
        Text(
            "  or  ",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun SocialButton(
    text: String,
    backgroundColor: Color,
    iconUrl: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Logo placeholder (thay bằng AsyncImage + Coil)
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text.first().toString(), color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(12.dp))
            Text(text, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
