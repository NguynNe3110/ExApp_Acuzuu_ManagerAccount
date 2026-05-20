package com.example.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.Account
import com.example.app.domain.model.AccountType
import com.example.app.domain.model.Category
import com.example.app.domain.model.PasswordLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// HomeViewModel.kt — Quản lý state màn hình Home
// ─────────────────────────────────────────────────────────────

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadCategories()
    }

    // ── Mock data — thay bằng Repository sau ──────────────────
    private fun loadCategories() {
        val mockAccounts = listOf(
            Account(1, "nguyen12112005@gmail.com", PasswordLevel.LEVEL_3),
            Account(2, "nguyen12112005nguyen@gmail.co...", PasswordLevel.LEVEL_2),
            Account(3, "0329223075", PasswordLevel.LEVEL_4),
        )
        val mockCategories = listOf(
            Category(1, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
            Category(2, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts),
            Category(3, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
            Category(4, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
            Category(5, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts),
            Category(6, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
            Category(7, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
            Category(8, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts),
            Category(9, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
        )
        _uiState.update { it.copy(categories = mockCategories) }
    }

    // ── User actions ───────────────────────────────────────────

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onToggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun onToggleViewMenu() {
        _uiState.update { it.copy(isViewMenuOpen = !it.isViewMenuOpen, isFabExpanded = false) }
    }

    fun onSelectViewMode(mode: ViewMode) {
        _uiState.update { it.copy(viewMode = mode, isViewMenuOpen = false) }
    }

    fun onToggleFab() {
        _uiState.update { it.copy(isFabExpanded = !it.isFabExpanded, isViewMenuOpen = false) }
    }

    fun onDismissFab() {
        _uiState.update { it.copy(isFabExpanded = false) }
    }

    fun onAddAccountClick() {
        _uiState.update { it.copy(isFabExpanded = false, showAddAccountDialog = true) }
    }

    fun onAddCategoryClick() {
        _uiState.update { it.copy(isFabExpanded = false, showAddCategoryDialog = true) }
    }

    fun onDismissDialogs() {
        _uiState.update { it.copy(showAddAccountDialog = false, showAddCategoryDialog = false) }
    }

    fun onSaveNewAccount(name: String, type: AccountType, level: PasswordLevel) {
        viewModelScope.launch {
            // Tìm hoặc tạo category theo type
            val existing = _uiState.value.categories.firstOrNull { it.name == type.label }
            val newAccount = Account(
                id            = System.currentTimeMillis().toInt(),
                displayName   = name,
                passwordLevel = level
            )
            if (existing != null) {
                val updated = _uiState.value.categories.map { cat ->
                    if (cat.id == existing.id) cat.copy(accounts = cat.accounts + newAccount)
                    else cat
                }
                _uiState.update { it.copy(categories = updated, showAddAccountDialog = false) }
            } else {
                val newCat = Category(
                    id       = System.currentTimeMillis().toInt(),
                    name     = type.label,
                    logoUrl  = null,
                    accounts = listOf(newAccount)
                )
                _uiState.update { it.copy(
                    categories = it.categories + newCat,
                    showAddAccountDialog = false
                )}
            }
            _events.send(HomeUiEvent.ShowSnackbar("Đã thêm tài khoản"))
        }
    }

    fun onSaveNewCategory(name: String) {
        viewModelScope.launch {
            val newCat = Category(
                id      = System.currentTimeMillis().toInt(),
                name    = name,
                logoUrl = null
            )
            _uiState.update { it.copy(
                categories = it.categories + newCat,
                showAddCategoryDialog = false
            )}
            _events.send(HomeUiEvent.ShowSnackbar("Đã thêm thư mục \"$name\""))
        }
    }

    fun onCategoryClick(categoryId: Int) {
        viewModelScope.launch {
            _events.send(HomeUiEvent.NavigateToCategoryDetail(categoryId))
        }
    }
}
