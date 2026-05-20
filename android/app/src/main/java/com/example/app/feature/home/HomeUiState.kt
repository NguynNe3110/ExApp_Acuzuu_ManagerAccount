package com.example.app.feature.home

import com.example.app.domain.model.ServiceProvider

// ─────────────────────────────────────────────────────────────
// HomeUiState.kt — Trạng thái UI của màn hình Home
//
// UI State là "snapshot" của màn hình tại một thời điểm.
// Compose sẽ recompose (vẽ lại) khi state thay đổi.
//
// Nguyên tắc: UI là hàm của State
//   UI = f(State)
//   → Thay vì "ẩn button này", "hiện loading kia"
//   → Chỉ cần cập nhật state, Compose tự biết vẽ gì
// ─────────────────────────────────────────────────────────────

data class HomeUiState(
    val isLoading: Boolean = false,
    val providers: List<ServiceProvider> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val errorMessage: String? = null,

    // Trạng thái phân trang
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val isLoadingMore: Boolean = false
) {
    // Computed property — tự tính từ state hiện tại
    val isEmpty: Boolean get() = !isLoading && providers.isEmpty() && errorMessage == null
    val isError: Boolean get() = errorMessage != null
}
