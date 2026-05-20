package com.example.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.core.result.Result
import com.example.app.domain.usecase.GetProvidersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// HomeViewModel.kt
//
// ViewModel chịu trách nhiệm:
//   1. Giữ UiState (sống qua screen rotation)
//   2. Xử lý user actions (loadProviders, search, ...)
//   3. Giao tiếp với UseCase/Repository
//   4. KHÔNG biết gì về Compose hay View
//
// Flow dữ liệu:
//   UseCase → Result<T> → ViewModel xử lý → update UiState → UI recompose
// ─────────────────────────────────────────────────────────────

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProvidersUseCase: GetProvidersUseCase
) : ViewModel() {

    // ── UiState: dùng StateFlow để Compose collect ────────────
    // MutableStateFlow: internal (chỉ ViewModel thay đổi được)
    private val _uiState = MutableStateFlow(HomeUiState())
    // StateFlow: expose ra ngoài (UI chỉ đọc)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // ── UiEvent: Channel đảm bảo event chỉ consume 1 lần ──────
    private val _events = Channel<HomeUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ── Search query flow để debounce ──────────────────────────
    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadProviders()
        observeSearch()
    }

    // ── Load providers lần đầu ─────────────────────────────────

    fun loadProviders(category: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getProvidersUseCase(page = 1, category = category)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            providers = result.data,
                            currentPage = 1
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                    _events.send(HomeUiEvent.ShowSnackbar(result.message))
                }
                is Result.Loading -> Unit // Không xảy ra ở đây
            }
        }
    }

    // ── Load thêm trang (pagination) ───────────────────────────

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMorePages) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            val nextPage = state.currentPage + 1
            when (val result = getProvidersUseCase(page = nextPage)) {
                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoadingMore = false,
                            providers = currentState.providers + result.data, // Append vào list cũ
                            currentPage = nextPage,
                            hasMorePages = result.data.isNotEmpty()
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                    _events.send(HomeUiEvent.ShowSnackbar(result.message))
                }
                is Result.Loading -> Unit
            }
        }
    }

    // ── Xử lý search với debounce ──────────────────────────────

    fun onSearchQueryChange(query: String) {
        // Cập nhật state để UI hiện text đang gõ ngay lập tức
        _uiState.update { it.copy(searchQuery = query) }
        // Gửi vào flow để debounce (không gọi API mỗi lần gõ)
        searchQueryFlow.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(400)          // Chờ 400ms sau lần gõ cuối mới search
                .distinctUntilChanged() // Không search lại nếu query giống nhau
                .collect { query ->
                    if (query.isBlank()) {
                        loadProviders() // Quay về danh sách gốc
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true) }
        // Search logic có thể mở rộng thêm ở đây
        loadProviders()
    }

    // ── User actions ───────────────────────────────────────────

    fun onProviderClick(providerId: Int) {
        viewModelScope.launch {
            _events.send(HomeUiEvent.NavigateToDetail(providerId))
        }
    }

    fun onCategorySelect(category: String?) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadProviders(category = category)
    }

    fun onRetry() {
        loadProviders()
    }
}
