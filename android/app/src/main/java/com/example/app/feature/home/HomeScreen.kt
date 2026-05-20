package com.example.app.feature.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.core.ui.*
import com.example.app.domain.model.Category
import com.example.app.ui.component.AddAccountDialog
import com.example.app.ui.component.AddCategoryDialog
import com.example.app.ui.component.PasswordStrengthIndicator

// ─────────────────────────────────────────────────────────────
// HomeScreen.kt — Màn hình chính Acuzu
//
// Đúng theo Figma:
//  • Header: logo "acuzu", dark/light toggle, search bar, sort icon
//  • Grid 3 cột HOẶC List 1 cột (toggle)
//  • Dropdown "Sắp xếp theo list / thẻ"
//  • FAB tròn ở giữa bottom nav, expand → 2 sub-FAB
//  • Bottom nav: Home | [+FAB] | Personal
// ─────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    onNavigateToCategory: (Int) -> Unit,
    onNavigateToPersonal: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is HomeUiEvent.NavigateToCategoryDetail -> onNavigateToCategory(event.categoryId)
                is HomeUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // Đóng FAB / menu khi tap ra ngoài
    if (uiState.isFabExpanded || uiState.isViewMenuOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(indication = null, interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }) {
                    viewModel.onDismissFab()
                    if (uiState.isViewMenuOpen) viewModel.onToggleViewMenu()
                }
        )
    }

    AppTheme(darkMode = uiState.isDarkMode) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                AcuzuBottomBar(
                    isFabExpanded = uiState.isFabExpanded,
                    onHomeClick = { /* already home */ },
                    onFabClick = viewModel::onToggleFab,
                    onPersonalClick = onNavigateToPersonal,
                    onAddAccountClick = viewModel::onAddAccountClick,
                    onAddCategoryClick = viewModel::onAddCategoryClick
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                // ── Header ───────────────────────────────────
                HomeHeader(
                    isDarkMode = uiState.isDarkMode,
                    onToggleDark = viewModel::onToggleDarkMode
                )
                Spacer(Modifier.height(12.dp))

                // ── Search bar + Sort icon ────────────────────
                SearchRow(
                    query = uiState.searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange,
                    isMenuOpen = uiState.isViewMenuOpen,
                    onSortClick = viewModel::onToggleViewMenu,
                    onSelectList = { viewModel.onSelectViewMode(ViewMode.LIST) },
                    onSelectGrid = { viewModel.onSelectViewMode(ViewMode.GRID) }
                )
                Spacer(Modifier.height(16.dp))

                // ── Category grid / list ──────────────────────
                when (uiState.viewMode) {
                    ViewMode.GRID -> CategoryGrid(
                        categories = uiState.filteredCategories,
                        onClick = { viewModel.onCategoryClick(it.id) }
                    )
                    ViewMode.LIST -> CategoryList(
                        categories = uiState.filteredCategories,
                        onClick = { viewModel.onCategoryClick(it.id) }
                    )
                }
            }
        }

        // ── Dialogs ───────────────────────────────────────────
        if (uiState.showAddAccountDialog) {
            AddAccountDialog(
                onDismiss = viewModel::onDismissDialogs,
                onSave = viewModel::onSaveNewAccount
            )
        }
        if (uiState.showAddCategoryDialog) {
            AddCategoryDialog(
                onDismiss = viewModel::onDismissDialogs,
                onSave = viewModel::onSaveNewCategory
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Sub-composables
// ─────────────────────────────────────────────────────────────

@Composable
private fun HomeHeader(isDarkMode: Boolean, onToggleDark: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo "acuzu" — teal dot + text
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(BluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text("a", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(Modifier.width(6.dp))
            Text("acuzu", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(Modifier.weight(1f))

        // Dark / Light mode toggle (pill shape)
        DarkModeToggle(isDarkMode = isDarkMode, onToggle = onToggleDark)
    }
}

@Composable
private fun DarkModeToggle(isDarkMode: Boolean, onToggle: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.clickable(onClick = onToggle)
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            // Sun icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (!isDarkMode) MaterialTheme.colorScheme.surface else Color.Transparent),
                contentAlignment = Alignment.Center
            ) { Text("☀", fontSize = 14.sp) }
            // Moon icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) MaterialTheme.colorScheme.surface else Color.Transparent),
                contentAlignment = Alignment.Center
            ) { Text("🌙", fontSize = 14.sp) }
        }
    }
}

@Composable
private fun SearchRow(
    query: String,
    onQueryChange: (String) -> Unit,
    isMenuOpen: Boolean,
    onSortClick: () -> Unit,
    onSelectList: () -> Unit,
    onSelectGrid: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Search bar
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search", color = MaterialTheme.colorScheme.outline) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
            singleLine = true,
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f).height(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )
        Spacer(Modifier.width(8.dp))

        // Sort icon + dropdown
        Box {
            IconButton(onClick = onSortClick) {
                Text("⚡", fontSize = 20.sp) // AI/sort spark icon
            }
            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = onSortClick
            ) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sắp xếp theo list")
                            Spacer(Modifier.width(8.dp))
                            Text("≡", fontSize = 16.sp)
                        }
                    },
                    onClick = onSelectList
                )
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sắp xếp theo thẻ")
                            Spacer(Modifier.width(8.dp))
                            Text("⊞", fontSize = 16.sp)
                        }
                    },
                    onClick = onSelectGrid
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid(categories: List<Category>, onClick: (Category) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.id }) { cat ->
            CategoryGridCard(category = cat, onClick = { onClick(cat) })
        }
    }
}

@Composable
private fun CategoryGridCard(category: Category, onClick: () -> Unit) {
    // Màu nền xen kẽ: Facebook = BlueLight, còn lại = GreenLight
    val bgColor = if (category.name.equals("Facebook", ignoreCase = true)) BlueLight else GreenLight
    Card(
        modifier = Modifier.aspectRatio(0.85f).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo circle
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name.first().toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (category.name.equals("Facebook", ignoreCase = true)) Color(0xFF1877F2) else Color(0xFF4285F4)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun CategoryList(categories: List<Category>, onClick: (Category) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories, key = { it.id }) { cat ->
            val bgColor = if (cat.name.equals("Facebook", ignoreCase = true)) BlueLight else GreenLight
            Card(
                modifier = Modifier.fillMaxWidth().height(60.dp).clickable { onClick(cat) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = bgColor),
                elevation = CardDefaults.cardElevation(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat.name.first().toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (cat.name.equals("Facebook", ignoreCase = true)) Color(0xFF1877F2) else Color(0xFF4285F4)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(cat.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Bottom Navigation + FAB
// ─────────────────────────────────────────────────────────────

@Composable
private fun AcuzuBottomBar(
    isFabExpanded: Boolean,
    onHomeClick: () -> Unit,
    onFabClick: () -> Unit,
    onPersonalClick: () -> Unit,
    onAddAccountClick: () -> Unit,
    onAddCategoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Nav bar background
        Surface(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Home tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(onClick = onHomeClick)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Home", tint = MaterialTheme.colorScheme.onSurface)
                    Text("Home", style = MaterialTheme.typography.labelSmall)
                }

                // Spacer for FAB
                Spacer(Modifier.width(56.dp))

                // Personal tab
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(onClick = onPersonalClick)
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Personal", tint = MaterialTheme.colorScheme.onSurface)
                    Text("Personal", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Sub-FAB buttons (animated, hiện khi expanded)
        AnimatedVisibility(
            visible = isFabExpanded,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = (-72).dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Add account
                FloatingActionButton(
                    onClick = onAddAccountClick,
                    modifier = Modifier.size(48.dp),
                    containerColor = BluePrimary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Thêm tài khoản", tint = Color.White)
                }
                // Add folder
                FloatingActionButton(
                    onClick = onAddCategoryClick,
                    modifier = Modifier.size(48.dp),
                    containerColor = BluePrimary,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.CreateNewFolder, contentDescription = "Thêm thư mục", tint = Color.White)
                }
            }
        }

        // Main FAB (+ / X)
        FloatingActionButton(
            onClick = onFabClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .size(56.dp)
                .shadow(4.dp, CircleShape),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .shadow(2.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isFabExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = if (isFabExpanded) "Đóng" else "Thêm",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
