# Cấu trúc dự án Android — Acuzu App

```
android/app/src/main/java/com/example/app/
│
├── core/                          # Dùng chung toàn app
│   ├── di/
│   │   ├── AppModule.kt           # Bind DispatcherProvider
│   │   ├── NetworkModule.kt       # Cung cấp Retrofit, API instances
│   │   └── RepositoryModule.kt    # Bind interface → implementation
│   ├── dispatcher/
│   │   └── DispatcherProvider.kt  # Quản lý coroutine dispatchers (testable)
│   ├── extension/
│   │   └── FlowExtensions.kt      # Flow.asResult() helper
│   ├── result/
│   │   └── Result.kt              # Sealed class: Success / Error / Loading
│   ├── ui/
│   │   └── Theme.kt               # Material3 theme
│   └── MyApp.kt                   # @HiltAndroidApp Application class
│
├── data/                          # Tầng data — biết về network/DB
│   ├── local/
│   │   ├── dao/
│   │   │   └── ServiceProviderDao.kt      # Room queries
│   │   ├── entity/
│   │   │   └── ServiceProviderEntity.kt   # Room table
│   │   └── AppDatabase.kt                 # RoomDatabase
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt                 # Retrofit endpoints auth
│   │   │   └── ServiceProviderApi.kt      # Retrofit endpoints providers
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   │   └── LoginRequestDto.kt
│   │   │   ├── response/
│   │   │   │   ├── LoginResponseDto.kt
│   │   │   │   └── ServiceProviderDto.kt
│   │   │   └── BaseResponseDto.kt         # Wrapper chung
│   │   ├── AuthInterceptor.kt             # Tự đính Bearer token
│   │   └── RetrofitProvider.kt            # Cấu hình OkHttp + Retrofit
│   ├── mapper/
│   │   ├── ServiceProviderMapper.kt       # DTO → Domain Model
│   │   └── UserMapper.kt
│   ├── repository/
│   │   ├── AuthRepositoryImpl.kt          # Implementation auth
│   │   └── ServiceProviderRepositoryImpl.kt
│   └── session/
│       └── SessionManager.kt              # DataStore: lưu token
│
├── domain/                        # Tầng domain — pure Kotlin, không Android
│   ├── model/
│   │   ├── ServiceProvider.kt     # Domain model
│   │   └── User.kt
│   ├── repository/
│   │   ├── AuthRepository.kt      # Interface (không biết impl)
│   │   └── ServiceProviderRepository.kt
│   └── usecase/
│       ├── GetProvidersUseCase.kt  # Sort featured + pagination
│       └── LoginUseCase.kt         # Validate + login
│
└── feature/                       # Tính năng — UI + ViewModel
    ├── auth/
    │   └── login/
    │       ├── LoginScreen.kt      # @Composable UI
    │       ├── LoginViewModel.kt   # @HiltViewModel
    │       ├── LoginUiState.kt     # data class state
    │       └── LoginUiEvent.kt     # sealed class events
    ├── home/
    │   ├── HomeScreen.kt
    │   ├── HomeViewModel.kt
    │   ├── HomeUiState.kt
    │   └── HomeUiEvent.kt
    └── main/
        ├── MainActivity.kt         # Single Activity entry point
        └── navigation/
            └── AppNavGraph.kt      # NavHost + routes
```

## Nguyên tắc phụ thuộc (Dependency Rule)

```
UI (feature)
    ↓ phụ thuộc vào
Domain (model, repository interface, usecase)
    ↑ KHÔNG biết đến
Data (repository impl, api, dto, room)
```

- `feature` biết `domain`, KHÔNG biết `data`  
- `data` biết `domain`, KHÔNG biết `feature`  
- `domain` KHÔNG biết ai cả (pure Kotlin)  
- `core` được dùng bởi tất cả các tầng
