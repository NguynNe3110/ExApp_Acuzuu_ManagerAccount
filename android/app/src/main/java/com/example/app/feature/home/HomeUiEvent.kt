package com.example.app.feature.home

// ─────────────────────────────────────────────────────────────
// HomeUiEvent.kt — Sự kiện 1 lần từ HomeViewModel
// ─────────────────────────────────────────────────────────────

sealed class HomeUiEvent {
    data class NavigateToCategoryDetail(val categoryId: Int) : HomeUiEvent()
    data class ShowSnackbar(val message: String) : HomeUiEvent()
}
