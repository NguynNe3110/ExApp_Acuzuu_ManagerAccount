package com.example.app.feature.home

import com.example.app.domain.model.Category

// ─────────────────────────────────────────────────────────────
// HomeUiState.kt — Trạng thái màn hình Home
// ─────────────────────────────────────────────────────────────

enum class ViewMode { GRID, LIST }

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val viewMode: ViewMode = ViewMode.GRID,
    val isDarkMode: Boolean = false,
    val isViewMenuOpen: Boolean = false,  // dropdown "Sắp xếp theo list/thẻ"
    val isFabExpanded: Boolean = false,   // FAB đang mở rộng hay không
    val showAddAccountDialog: Boolean = false,
    val showAddCategoryDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredCategories: List<Category>
        get() = if (searchQuery.isBlank()) categories
                else categories.filter { it.name.contains(searchQuery, ignoreCase = true) }
}
