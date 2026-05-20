# Luồng dữ liệu: API → Repository → ViewModel → UI

```
┌─────────────────────────────────────────────────────────────────────┐
│                         USER ACTION                                  │
│               (ví dụ: mở app, scroll, nhấn nút)                     │
└─────────────────────────┬───────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    COMPOSE UI (HomeScreen.kt)                        │
│                                                                      │
│  val uiState by viewModel.uiState.collectAsStateWithLifecycle()      │
│                                                                      │
│  when {                                                              │
│    uiState.isLoading → CircularProgressIndicator()                   │
│    uiState.isError   → ErrorState(uiState.errorMessage)             │
│    else              → ProviderGrid(uiState.providers)               │
│  }                                                                   │
└─────────────────────────┬───────────────────────────────────────────┘
                          │  collectAsStateWithLifecycle() đọc StateFlow
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                   VIEWMODEL (HomeViewModel.kt)                       │
│                                                                      │
│  _uiState: MutableStateFlow<HomeUiState>  (internal)                 │
│   uiState: StateFlow<HomeUiState>         (exposed)                  │
│                                                                      │
│  fun loadProviders() {                                               │
│    _uiState.update { it.copy(isLoading = true) }                     │
│    when (val result = getProvidersUseCase()) {                       │
│      Success → _uiState.update { copy(providers = result.data) }    │
│      Error   → _uiState.update { copy(errorMessage = result.msg) }  │
│    }                                                                 │
│  }                                                                   │
└─────────────────────────┬───────────────────────────────────────────┘
                          │  suspend fun invoke()
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│                  USE CASE (GetProvidersUseCase.kt)                   │
│                                                                      │
│  Business rule: sort featured first, then by name                    │
│                                                                      │
│  suspend operator fun invoke(): Result<List<ServiceProvider>> {      │
│    val result = repository.getProviders()                            │
│    return result.map { list → list.sortedBy {...} }                  │
│  }                                                                   │
└─────────────────────────┬───────────────────────────────────────────┘
                          │  suspend fun getProviders()
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│           REPOSITORY INTERFACE (ServiceProviderRepository.kt)        │
│                    [domain layer - pure Kotlin]                      │
│                                                                      │
│  interface ServiceProviderRepository {                               │
│    suspend fun getProviders(): Result<List<ServiceProvider>>         │
│  }                                                                   │
└─────────────────────────┬───────────────────────────────────────────┘
                          │  Hilt inject impl → interface
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────────┐
│        REPOSITORY IMPL (ServiceProviderRepositoryImpl.kt)            │
│                    [data layer]                                      │
│                                                                      │
│  try {                                                               │
│    val dto = api.getProviders()     // gọi Retrofit                 │
│    Result.Success(dto.toDomain())   // DTO → Domain Model           │
│  } catch (e: Exception) {                                            │
│    Result.Error(e.message)                                           │
│  }                                                                   │
└─────────────────────────┬───────────────────────────────────────────┘
                          │  Mapper: DTO → Domain Model
                          │
        ┌─────────────────┴──────────────────┐
        │                                    │
        ▼                                    ▼
┌──────────────────┐              ┌──────────────────────┐
│  RETROFIT API    │              │    ROOM DAO           │
│ (ServiceProvider │              │ (ServiceProviderDao)  │
│      Api.kt)     │              │                       │
│                  │              │  Cache offline data   │
│  suspend fun     │              │  Flow<List<Entity>>   │
│  getProviders()  │              │                       │
└──────────────────┘              └──────────────────────┘
        │                                    │
        ▼                                    ▼
┌──────────────────┐              ┌──────────────────────┐
│   NETWORK        │              │   SQLITE (local)     │
│  (Internet)      │              │                       │
└──────────────────┘              └──────────────────────┘
```

## Giải thích từng bước

### Bước 1 — UI gọi ViewModel
```kotlin
// HomeScreen.kt
LaunchedEffect(Unit) {
    viewModel.loadProviders() // UI trigger action
}
```

### Bước 2 — ViewModel cập nhật Loading state
```kotlin
// HomeViewModel.kt
_uiState.update { it.copy(isLoading = true) }
```
→ UI ngay lập tức recompose, hiển thị `CircularProgressIndicator`

### Bước 3 — ViewModel gọi UseCase
```kotlin
val result = getProvidersUseCase(page = 1)
```

### Bước 4 — UseCase gọi Repository (interface)
```kotlin
val result = repository.getProviders()
// ViewModel không biết impl nào đang chạy!
```

### Bước 5 — Repository gọi Retrofit API
```kotlin
val response = api.getProviders() // Chạy trên Dispatchers.IO
```

### Bước 6 — Map DTO → Domain Model
```kotlin
response.data.toDomain() // List<DTO> → List<ServiceProvider>
```

### Bước 7 — ViewModel nhận Result, cập nhật State
```kotlin
is Result.Success → _uiState.update { it.copy(providers = result.data, isLoading = false) }
is Result.Error   → _uiState.update { it.copy(errorMessage = result.message) }
```

### Bước 8 — UI tự động recompose
```kotlin
// Compose tự detect StateFlow thay đổi và vẽ lại
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
// → ProviderGrid(uiState.providers) được vẽ với data mới
```

## Tại sao KHÔNG dùng viewBinding?

| | viewBinding (XML) | Jetpack Compose |
|---|---|---|
| Layout | XML file | Kotlin function |
| Update UI | `binding.textView.text = "..."` | Cập nhật State → UI tự recompose |
| Adapter | RecyclerView.Adapter + ViewHolder | `LazyColumn/LazyVerticalGrid` |
| findViewByID | Không cần (viewBinding) | Không có khái niệm này |
| Compose | **Không dùng viewBinding** | setContent { } thay hết |

> viewBinding chỉ cần thiết khi bạn dùng XML layout.  
> Trong project này dùng 100% Compose → không cần viewBinding.
