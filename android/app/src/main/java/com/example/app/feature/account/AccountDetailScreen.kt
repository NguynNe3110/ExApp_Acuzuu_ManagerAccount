package com.example.app.feature.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.core.ui.BluePrimary
import com.example.app.domain.model.Account
import com.example.app.domain.model.CustomField
import com.example.app.ui.component.SingleFieldDialog

// ─────────────────────────────────────────────────────────────
// AccountDetailScreen.kt — Chi tiết một tài khoản
//
// Đúng theo Figma:
//  • AppBar: Back, tên category (vd "Google"), ⚙ icon
//  • Card xanh viền: tên tài khoản + "Mức X"
//  • Danh sách fields tùy chỉnh: Label + OutlinedTextField + ⋮
//  • ⋮ trên field → dropdown "Chỉnh sửa" / "Xóa"
//  • "Ghi chú" textarea ở dưới cùng
//  • Nút Save xanh to
//  • FAB + tròn góc phải dưới → thêm field mới
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    account: Account,
    categoryName: String,
    onBack: () -> Unit
) {
    var fields by remember {
        mutableStateOf(
            account.customFields.ifEmpty {
                listOf(
                    CustomField(1, "Username", ""),
                    CustomField(2, "Xác thực 2 lớp", ""),
                )
            }
        )
    }
    var notes by remember { mutableStateOf(account.notes) }
    var showAddFieldDialog by remember { mutableStateOf(false) }
    // Index của field đang mở menu
    var openMenuIndex by remember { mutableStateOf<Int?>(null) }
    // Index field đang edit
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(categoryName, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                        Text("Back", style = MaterialTheme.typography.bodyMedium)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddFieldDialog = true },
                containerColor = BluePrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(52.dp)
            ) {
                Icon(Icons.Default.Add, "Thêm field", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Card tên tài khoản + mức độ ────────────────
            item {
                AccountHeaderCard(
                    displayName   = account.displayName,
                    levelLabel    = account.passwordLevel.label
                )
            }

            // ── Các field tùy chỉnh ─────────────────────────
            itemsIndexed(fields, key = { _, f -> f.id }) { index, field ->
                CustomFieldItem(
                    field = field,
                    isMenuOpen = openMenuIndex == index,
                    onMenuOpen = { openMenuIndex = index },
                    onMenuDismiss = { openMenuIndex = null },
                    onEdit = {
                        openMenuIndex = null
                        editingIndex = index
                    },
                    onDelete = {
                        openMenuIndex = null
                        fields = fields.toMutableList().also { it.removeAt(index) }
                    },
                    onValueChange = { newVal ->
                        fields = fields.toMutableList().also {
                            it[index] = field.copy(value = newVal)
                        }
                    }
                )
            }

            // ── Ghi chú textarea ────────────────────────────
            item {
                Text("Ghi chú", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("note", color = MaterialTheme.colorScheme.outline) },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // ── Nút Save ────────────────────────────────────
            item {
                Button(
                    onClick = { /* TODO: save */ onBack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Text("Save", color = Color.White, fontSize = 16.sp)
                }
                Spacer(Modifier.height(80.dp)) // Space cho FAB
            }
        }
    }

    // Dialog nhập tên field mới
    if (showAddFieldDialog) {
        SingleFieldDialog(
            title = "Trường dữ liệu",
            onDismiss = { showAddFieldDialog = false },
            onSave = { label ->
                fields = fields + CustomField(
                    id    = System.currentTimeMillis().toInt(),
                    label = label,
                    value = ""
                )
                showAddFieldDialog = false
            }
        )
    }

    // Dialog chỉnh sửa label field
    if (editingIndex != null) {
        val idx = editingIndex!!
        SingleFieldDialog(
            title = "Trường dữ liệu",
            onDismiss = { editingIndex = null },
            onSave = { newLabel ->
                fields = fields.toMutableList().also {
                    it[idx] = fields[idx].copy(label = newLabel)
                }
                editingIndex = null
            }
        )
    }
}

@Composable
private fun AccountHeaderCard(displayName: String, levelLabel: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, BluePrimary)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = levelLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun CustomFieldItem(
    field: CustomField,
    isMenuOpen: Boolean,
    onMenuOpen: () -> Unit,
    onMenuDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = field.label,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(onClick = onMenuOpen) {
                    Icon(Icons.Default.MoreVert, "Menu", modifier = Modifier.size(20.dp))
                }
                // Dropdown "Chỉnh sửa" / "Xóa"
                DropdownMenu(expanded = isMenuOpen, onDismissRequest = onMenuDismiss) {
                    DropdownMenuItem(text = { Text("Chỉnh sửa") }, onClick = onEdit)
                    DropdownMenuItem(text = { Text("Xóa") }, onClick = onDelete)
                }
            }
        }
        OutlinedTextField(
            value = field.value,
            onValueChange = onValueChange,
            placeholder = { Text("Username", color = MaterialTheme.colorScheme.outline) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}
