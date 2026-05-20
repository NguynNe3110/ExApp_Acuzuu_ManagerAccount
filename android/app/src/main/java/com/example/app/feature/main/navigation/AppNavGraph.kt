package com.example.app.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.feature.auth.login.LoginScreen
import com.example.app.feature.home.HomeScreen

// ─────────────────────────────────────────────────────────────
// AppNavGraph.kt — Định nghĩa navigation graph của toàn app
//
// NavController: quản lý back stack và điều hướng
// NavHost: container hiển thị composable theo route hiện tại
//
// Mỗi "màn hình" = một composable được đăng ký với route string
// ─────────────────────────────────────────────────────────────

// Định nghĩa tất cả routes ở đây để tránh hardcode string
object AppRoutes {
    const val LOGIN = "login"
    const val HOME  = "home"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN // Màn hình đầu tiên khi mở app
    ) {

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onNavigateToHome = {
                    // popUpTo xóa LoginScreen khỏi back stack
                    // → Nhấn Back ở Home sẽ thoát app, không quay lại Login
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.HOME) {
            HomeScreen(
                onNavigateToDetail = { providerId ->
                    // Điều hướng đến màn hình chi tiết với tham số
                    // navController.navigate("${AppRoutes.DETAIL}/$providerId")
                }
            )
        }
    }
}
