package com.example.app.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.app.core.ui.BluePrimary
import com.example.app.domain.model.AccountType
import com.example.app.domain.model.PasswordLevel

// ─────────────────────────────────────────────────────────────
// AcuzuDialog.kt — Các dialog dùng chung trong app
// ─────────────────────────────────────────────────────────────

/** Dialog thêm tài khoản mới (từ Home) */
@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, type: AccountType, level: PasswordLevel) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AccountType.OTHER) }
    var selectedLevel by remember { mutableStateOf(PasswordLevel.LEVEL_3) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var levelMenuExpanded by remember { mutableStateOf(false) }

    AcuzuBaseDialog(onDismiss = onDismiss) {
        Text("Tên tài khoản", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(Modifier.height(16.dp))

        // Loại dropdown
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Loại", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Box {
                OutlinedButton(onClick = { typeMenuExpanded = true }) {
                    Text(selectedType.label)
                    Text("  ∨")
                }
                DropdownMenu(expanded = typeMenuExpanded, onDismissRequest = { typeMenuExpanded = false }) {
                    AccountType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.label) },
                            onClick = { selectedType = type; typeMenuExpanded = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        // Mức độ mật khẩu dropdown
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mức độ mật khẩu", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Box {
                OutlinedButton(onClick = { levelMenuExpanded = true }) {
                    Text(selectedLevel.label)
                    Text("  ∨")
                }
                DropdownMenu(expanded = levelMenuExpanded, onDismissRequest = { levelMenuExpanded = false }) {
                    PasswordLevel.entries.forEach { lvl ->
                        DropdownMenuItem(
                            text = { Text(lvl.label) },
                            onClick = { selectedLevel = lvl; levelMenuExpanded = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        DialogButtons(onDismiss = onDismiss, onSave = { onSave(name, selectedType, selectedLevel) })
    }
}

/** Dialog thêm thư mục mới */
@Composable
fun AddCategoryDialog(
    title: String = "Tên thư mục",
    onDismiss: () -> Unit,
    onSave: (name: String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AcuzuBaseDialog(onDismiss = onDismiss) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        if (title == "Tên thư mục") {
            Spacer(Modifier.height(16.dp))
            Text("Logo", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Chọn từ thiết bị", color = MaterialTheme.colorScheme.outline)
            }
        }
        Spacer(Modifier.height(16.dp))
        DialogButtons(onDismiss = onDismiss, onSave = { onSave(name) })
    }
}

/** Dialog nhập 1 trường đơn giản (tên tài khoản trong category detail) */
@Composable
fun SingleFieldDialog(
    title: String,
    onDismiss: () -> Unit,
    onSave: (value: String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    AcuzuBaseDialog(onDismiss = onDismiss) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(Modifier.height(16.dp))
        DialogButtons(onDismiss = onDismiss, onSave = { onSave(value) })
    }
}

/** Dialog thêm tài khoản trong Category Detail */
@Composable
fun AddAccountInCategoryDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, level: PasswordLevel) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedLevel by remember { mutableStateOf(PasswordLevel.LEVEL_3) }
    var levelMenuExpanded by remember { mutableStateOf(false) }

    AcuzuBaseDialog(onDismiss = onDismiss) {
        Text("Tên tài khoản", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mức độ mật khẩu", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Box {
                OutlinedButton(onClick = { levelMenuExpanded = true }) {
                    Text(selectedLevel.label)
                    Text("  ∨")
                }
                DropdownMenu(expanded = levelMenuExpanded, onDismissRequest = { levelMenuExpanded = false }) {
                    PasswordLevel.entries.forEach { lvl ->
                        DropdownMenuItem(
                            text = { Text(lvl.label) },
                            onClick = { selectedLevel = lvl; levelMenuExpanded = false }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        DialogButtons(onDismiss = onDismiss, onSave = { onSave(name, selectedLevel) })
    }
}

/** Dialog chọn mức độ tối đa (Profile) */
@Composable
fun MaxLevelDialog(
    current: PasswordLevel,
    onDismiss: () -> Unit,
    onSave: (PasswordLevel) -> Unit
) {
    var selected by remember { mutableStateOf(current) }
    var menuExpanded by remember { mutableStateOf(false) }

    AcuzuBaseDialog(onDismiss = onDismiss) {
        Text("Mức độ tối đa", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Box {
            OutlinedButton(
                onClick = { menuExpanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(selected.label, modifier = Modifier.weight(1f))
                Text("∨")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                PasswordLevel.entries.forEach { lvl ->
                    DropdownMenuItem(
                        text = { Text(lvl.label) },
                        onClick = { selected = lvl; menuExpanded = false }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        DialogButtons(onDismiss = onDismiss, onSave = { onSave(selected) })
    }
}

// ── Shared helpers ─────────────────────────────────────────────

@Composable
private fun AcuzuBaseDialog(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(20.dp), content = content)
        }
    }
}

@Composable
private fun DialogButtons(onDismiss: () -> Unit, onSave: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(end = 8.dp)
        ) { Text("Cancel") }
        Button(
            onClick = onSave,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
        ) { Text("Save", color = Color.White) }
    }
}
