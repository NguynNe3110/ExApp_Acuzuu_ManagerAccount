package com.example.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

// ─────────────────────────────────────────────────────────────
// EditProfileScreen.kt — Chỉnh sửa hồ sơ
//
// Đúng theo Figma:
//  • AppBar: back + "My profile" + ⚙
//  • Avatar tròn + tên "Uzuu of kings"
//  • 4 field Username
//  • Nút Save xanh to
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    username: String = "Uzuu of kings",
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            // Avatar tròn
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7B68EE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.first().toString().uppercase(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))

            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            // 4 field Username (theo Figma)
            listOf(
                Triple("Username", name, { v: String -> name = v }),
                Triple("Username", email, { v: String -> email = v }),
                Triple("Username", phone, { v: String -> phone = v }),
                Triple("Username", bio, { v: String -> bio = v })
            ).forEach { (label, value, onValueChange) ->
                Text(label, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = { Text("Username", color = MaterialTheme.colorScheme.outline) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(Modifier.height(16.dp))
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Text("Save", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
