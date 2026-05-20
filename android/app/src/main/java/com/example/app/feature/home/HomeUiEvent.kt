package com.example.app.feature.home

// ─────────────────────────────────────────────────────────────
// HomeUiEvent.kt — Các sự kiện 1 lần từ ViewModel → UI
//
// Khác với UiState (trạng thái liên tục),
// UiEvent là sự kiện "bắn một lần" rồi thôi:
//   → Hiện Snackbar/Toast
//   → Navigate sang màn khác
//   → Show dialog
//
// Dùng Channel/SharedFlow để đảm bảo sự kiện chỉ được consume 1 lần
// ─────────────────────────────────────────────────────────────

sealed class HomeUiEvent {
    data class ShowSnackbar(val message: String) : HomeUiEvent()
    data class NavigateToDetail(val providerId: Int) : HomeUiEvent()
    data object NavigateToSearch : HomeUiEvent()
}
