package com.example.app.feature.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.app.domain.model.Account
import com.example.app.domain.model.Category
import com.example.app.domain.model.PasswordLevel
import com.example.app.ui.component.AddAccountInCategoryDialog
import com.example.app.ui.component.AddCategoryDialog
import com.example.app.ui.component.PasswordStrengthIndicator

// ─────────────────────────────────────────────────────────────
// CategoryDetailScreen.kt — Danh sách tài khoản trong 1 category
//
// Đúng theo Figma:
//  • AppBar: back arrow, tên category (VD: "Google"), ⋮ menu
//  • Danh sách account: text email/phone + strength indicator bên phải
//  • ⋮ menu → Thêm tài khoản / Thêm thư mục / Di chuyển
//  • Dialog thêm tài khoản + dialog thêm thư mục
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: Category,
    onBack: () -> Unit,
    onAccountClick: (Account) -> Unit
) {
    var accounts by remember { mutableStateOf(category.accounts) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var showAddAccountDialog by remember { mutableStateOf(false) }
    var showAddFolderDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category.name,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMoreMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        // ⋮ dropdown: Thêm tài khoản / Thêm thư mục / Di chuyển
                        DropdownMenu(
                            expanded = showMoreMenu,
                            onDismissRequest = { showMoreMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Thêm tài khoản") },
                                onClick = { showMoreMenu = false; showAddAccountDialog = true }
                            )
                            DropdownMenuItem(
                                text = { Text("Thêm thư mục") },
                                onClick = { showMoreMenu = false; showAddFolderDialog = true }
                            )
                            DropdownMenuItem(
                                text = { Text("Di chuyển") },
                                onClick = { showMoreMenu = false }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(accounts, key = { it.id }) { account ->
                AccountListItem(
                    account = account,
                    onClick = { onAccountClick(account) }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }
        }
    }

    // Dialogs
    if (showAddAccountDialog) {
        AddAccountInCategoryDialog(
            onDismiss = { showAddAccountDialog = false },
            onSave = { name, level ->
                accounts = accounts + Account(
                    id            = System.currentTimeMillis().toInt(),
                    displayName   = name,
                    passwordLevel = level
                )
                showAddAccountDialog = false
            }
        )
    }
    if (showAddFolderDialog) {
        AddCategoryDialog(
            title = "Tên thư mục",
            onDismiss = { showAddFolderDialog = false },
            onSave = { showAddFolderDialog = false }
        )
    }
}

@Composable
private fun AccountListItem(account: Account, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = account.displayName,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(12.dp))
        PasswordStrengthIndicator(level = account.passwordLevel)
    }
}
