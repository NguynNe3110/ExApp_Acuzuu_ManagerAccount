package com.example.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.core.ui.BluePrimary
import com.example.app.core.ui.RedPrimary
import com.example.app.domain.model.PasswordLevel
import com.example.app.domain.model.UserProfile
import com.example.app.ui.component.MaxLevelDialog

// ─────────────────────────────────────────────────────────────
// ProfileScreen.kt — Màn hình "My profile"
//
// Đúng theo Figma:
//  • AppBar: back + "My profile" + ⚙ icon
//  • Avatar tròn + tên "Uzuu of kings"
//  • Nút "Edit profile" màu đỏ
//  • Menu items với ♡ icon: Thiết lập mức độ, Favourites x7
//  • Logout (màu đỏ, icon mũi tên ra ngoài)
//  • Dialog "Mức độ tối đa" khi tap Thiết lập mức độ
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var profile by remember {
        mutableStateOf(UserProfile(1, "Uzuu of kings", null, PasswordLevel.LEVEL_3))
    }
    var showLevelDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("My profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // ── Avatar + tên + Edit button ─────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar tròn (placeholder)
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF7B68EE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.username.first().toString().uppercase(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile.username,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        // Nút Edit profile màu đỏ
                        Button(
                            onClick = onEditProfile,
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(40.dp)
                        ) {
                            Text("Edit profile", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Thiết lập mức độ ──────────────────────────
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Favorite,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    label = "Thiết lập mức độ",
                    onClick = { showLevelDialog = true }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }

            // ── Favourites items ─────────────────────────
            items(count = 7) { i ->
                ProfileMenuItem(
                    icon = Icons.Default.Favorite,
                    iconTint = MaterialTheme.colorScheme.onSurface,
                    label = "Favourites",
                    onClick = {}
                )
                if (i < 6) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                }
            }

            // ── Divider ─────────────────────────────────
            item { HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp)) }

            // ── Log out ──────────────────────────────────
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    iconTint = RedPrimary,
                    label = "Log out",
                    labelColor = RedPrimary,
                    onClick = onLogout
                )
            }
        }
    }

    // Dialog Mức độ tối đa
    if (showLevelDialog) {
        MaxLevelDialog(
            current = profile.maxPasswordLevel,
            onDismiss = { showLevelDialog = false },
            onSave = { level ->
                profile = profile.copy(maxPasswordLevel = level)
                showLevelDialog = false
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = iconTint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = labelColor, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
    }
}
