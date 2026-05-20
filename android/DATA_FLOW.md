# Luồng dữ liệu: API → Repository → ViewModel → UI

## App: Acuzu — Quản lý tài khoản

```
┌─────────────────────────────────────────────────────────────────┐
│                       USER ACTION                               │
│  (tap Login, tap Category card, tap + FAB, gõ tìm kiếm...)     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│                  COMPOSE UI (HomeScreen.kt)                     │
│                                                                 │
│  val uiState by viewModel.uiState.collectAsStateWithLifecycle() │
│                                                                 │
│  when (uiState.viewMode) {                                      │
│    GRID → CategoryGrid(uiState.filteredCategories)              │
│    LIST → CategoryList(uiState.filteredCategories)              │
│  }                                                              │
│                                                                 │
│  // FAB expanded state                                          │
│  if (uiState.isFabExpanded) → SubFabButtons()                   │
│  if (uiState.showAddAccountDialog) → AddAccountDialog()         │
└──────────────────────────┬──────────────────────────────────────┘
                           │  collectAsStateWithLifecycle()
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│               VIEWMODEL (HomeViewModel.kt)                      │
│                                                                 │
│  _uiState: MutableStateFlow<HomeUiState>  (private/mutable)    │
│   uiState: StateFlow<HomeUiState>         (public/read-only)    │
│                                                                 │
│  fun onToggleFab() {                                            │
│    _uiState.update { it.copy(isFabExpanded = !it.isFabExpanded)}│
│  }                                                              │
│                                                                 │
│  fun onSaveNewAccount(name, type, level) {                      │
│    // Business logic: tìm category phù hợp, thêm account       │
│    _uiState.update { it.copy(categories = updated) }           │
│    _events.send(HomeUiEvent.ShowSnackbar("Đã thêm"))            │
│  }                                                              │
└──────────────────────────┬──────────────────────────────────────┘
                           │  (gọi Repository/UseCase)
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│            REPOSITORY INTERFACE (domain layer)                  │
│                                                                 │
│  interface CategoryRepository {                                 │
│    suspend fun getCategories(): Result<List<Category>>          │
│    suspend fun addAccount(categoryId, account): Result<Unit>    │
│  }                                                              │
└──────────────────────────┬──────────────────────────────────────┘
                           │  Hilt inject impl → interface
                           │
                           ▼
┌─────────────────────────────────────────────────────────────────┐
│         REPOSITORY IMPL (data layer)                            │
│                                                                 │
│  // Nếu online: gọi API                                         │
│  val response = api.getCategories()                             │
│  Result.Success(response.data.map { it.toDomain() })            │
│                                                                 │
│  // Nếu offline: đọc từ Room cache                              │
│  dao.getAllCategories().map { entities ->                        │
│      entities.map { it.toDomain() }                             │
│  }                                                              │
└──────┬───────────────────────────┬──────────────────────────────┘
       │                           │
       ▼                           ▼
┌──────────────┐          ┌──────────────────┐
│  Retrofit    │          │   Room DAO        │
│  (Network)   │          │   (Local Cache)   │
└──────────────┘          └──────────────────┘
```

## Ví dụ cụ thể: Thêm tài khoản mới

```
1. User nhấn FAB (+)
   → viewModel.onToggleFab()
   → _uiState.update { isFabExpanded = true }
   → UI hiện 2 sub-FAB (account + folder)

2. User nhấn sub-FAB "Thêm tài khoản"
   → viewModel.onAddAccountClick()
   → _uiState.update { showAddAccountDialog = true }
   → UI hiện AddAccountDialog

3. User điền tên + chọn loại + chọn mức độ → nhấn Save
   → viewModel.onSaveNewAccount("gmail@...", AccountType.GOOGLE, PasswordLevel.LEVEL_3)
   → Business logic: tìm/tạo category Google
   → _uiState.update { categories = updatedList }
   → _events.send(ShowSnackbar("Đã thêm tài khoản"))
   → UI recompose: card Google xuất hiện trong grid
   → SnackBar hiện: "Đã thêm tài khoản"
```

## Tại sao dùng UiEvent thay vì UiState cho Snackbar?

```kotlin
// ❌ SAI: Snackbar trong State
data class HomeUiState(val snackbarMessage: String? = null)
// → Vì State là "liên tục", mỗi lần recompose đều check
// → Snackbar có thể hiện nhiều lần

// ✅ ĐÚNG: Snackbar trong Event (Channel)
sealed class HomeUiEvent {
    data class ShowSnackbar(val message: String) : HomeUiEvent()
}
// → Channel đảm bảo event chỉ được consume 1 lần duy nhất
```
