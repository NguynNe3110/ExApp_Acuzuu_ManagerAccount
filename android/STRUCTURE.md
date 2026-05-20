# Cấu trúc dự án Android — Acuzu (App quản lý tài khoản)

## Màn hình

| Màn hình | File | Mô tả |
|---|---|---|
| Login | `feature/auth/login/LoginScreen.kt` | Username + Password + social login |
| Register | `feature/auth/register/RegisterScreen.kt` | Đăng ký tài khoản mới |
| Home | `feature/home/HomeScreen.kt` | Grid/List danh mục + FAB + dark mode |
| Category Detail | `feature/category/CategoryDetailScreen.kt` | Danh sách accounts trong 1 category |
| Account Detail | `feature/account/AccountDetailScreen.kt` | Chi tiết tài khoản + custom fields |
| My Profile | `feature/profile/ProfileScreen.kt` | Hồ sơ + logout + thiết lập mức độ |
| Edit Profile | `feature/profile/EditProfileScreen.kt` | Chỉnh sửa thông tin cá nhân |

## Cấu trúc thư mục

```
android/app/src/main/java/com/example/app/
│
├── core/
│   ├── di/
│   │   ├── AppModule.kt           # DispatcherProvider binding
│   │   ├── NetworkModule.kt       # Retrofit + API instances
│   │   └── RepositoryModule.kt    # Interface → Implementation binding
│   ├── dispatcher/
│   │   └── DispatcherProvider.kt  # Coroutine dispatchers (testable)
│   ├── extension/
│   │   └── FlowExtensions.kt      # Flow.asResult() helper
│   ├── result/
│   │   └── Result.kt              # Sealed: Success / Error / Loading
│   ├── ui/
│   │   └── Theme.kt               # Material3 theme + màu Figma
│   └── MyApp.kt                   # @HiltAndroidApp entry point
│
├── data/
│   ├── local/
│   │   ├── dao/ServiceProviderDao.kt
│   │   ├── entity/ServiceProviderEntity.kt
│   │   └── AppDatabase.kt
│   ├── remote/
│   │   ├── api/AuthApi.kt
│   │   ├── api/ServiceProviderApi.kt
│   │   ├── api/RecipeApi.kt
│   │   ├── dto/BaseResponseDto.kt
│   │   ├── dto/request/LoginRequestDto.kt
│   │   ├── dto/response/LoginResponseDto.kt
│   │   ├── dto/response/ServiceProviderDto.kt
│   │   ├── dto/response/RecipeDto.kt
│   │   ├── AuthInterceptor.kt     # Auto-attach Bearer token
│   │   └── RetrofitProvider.kt    # OkHttp + Retrofit config
│   ├── mapper/
│   │   ├── ServiceProviderMapper.kt
│   │   ├── UserMapper.kt
│   │   └── RecipeMapper.kt
│   ├── repository/
│   │   ├── AuthRepositoryImpl.kt
│   │   └── ServiceProviderRepositoryImpl.kt
│   └── session/
│       └── SessionManager.kt      # DataStore token storage
│
├── domain/
│   ├── model/
│   │   ├── Account.kt             # Account, Category, PasswordLevel, CustomField
│   │   ├── UserProfile.kt         # Hồ sơ người dùng
│   │   ├── ServiceProvider.kt
│   │   └── User.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ServiceProviderRepository.kt
│   │   └── RecipeRepository.kt
│   └── usecase/
│       ├── GetProvidersUseCase.kt
│       └── LoginUseCase.kt
│
├── feature/
│   ├── auth/
│   │   ├── login/
│   │   │   ├── LoginScreen.kt      ← Màn hình Login
│   │   │   ├── LoginViewModel.kt
│   │   │   ├── LoginUiState.kt
│   │   │   └── LoginUiEvent.kt
│   │   └── register/
│   │       ├── RegisterScreen.kt   ← Màn hình Register
│   │       ├── RegisterUiState.kt
│   │       └── RegisterUiEvent.kt
│   ├── home/
│   │   ├── HomeScreen.kt           ← Grid/List + FAB + dark mode
│   │   ├── HomeViewModel.kt
│   │   ├── HomeUiState.kt
│   │   └── HomeUiEvent.kt
│   ├── category/
│   │   └── CategoryDetailScreen.kt ← Danh sách accounts
│   ├── account/
│   │   └── AccountDetailScreen.kt  ← Chi tiết + edit fields
│   ├── profile/
│   │   ├── ProfileScreen.kt        ← My profile
│   │   └── EditProfileScreen.kt    ← Edit profile
│   └── main/
│       ├── MainActivity.kt
│       └── navigation/AppNavGraph.kt  ← Routes + navigation
│
└── ui/
    ├── component/
    │   ├── PasswordStrengthIndicator.kt  ← Thanh chỉ báo mức mật khẩu
    │   └── AcuzuDialog.kt               ← Các dialog dùng chung
    └── adapter/
```

## Navigation Flow

```
Login ──────────────────────────────► Home
  │                                     │
  └──► Register ──────────────────►    │
                                        ├──► Category Detail ──► Account Detail
                                        │
                                        └──► Personal ──────────► Edit Profile
```

## Màu sắc (từ Figma)

| Tên | Hex | Dùng cho |
|---|---|---|
| BluePrimary | `#29B6F6` | Nút Login/Save, FAB |
| RedPrimary | `#E53935` | Edit profile, Log out |
| GreenLight | `#F5F8EE` | Card background Google |
| BlueLight | `#E3F8FF` | Card background Facebook |
| StrengthGreen | `#4CAF50` | Mức mật khẩu mạnh |
| StrengthYellow | `#FFEB3B` | Mức trung bình |
| StrengthRed | `#F44336` | Mức yếu |

## Password Strength Indicator

```
┌──┐  ← Mức 4 (xanh)
├──┤  ← Mức 3 (vàng)
├──┤  ← Mức 2 (cam)
└──┘  ← Mức 1 (đỏ)
```
Mức càng cao → càng nhiều đoạn có màu (từ dưới lên)
