package com.example.app.feature.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.app.domain.model.Account
import com.example.app.domain.model.Category
import com.example.app.domain.model.PasswordLevel
import com.example.app.feature.account.AccountDetailScreen
import com.example.app.feature.auth.login.LoginScreen
import com.example.app.feature.auth.register.RegisterScreen
import com.example.app.feature.category.CategoryDetailScreen
import com.example.app.feature.home.HomeScreen
import com.example.app.feature.profile.EditProfileScreen
import com.example.app.feature.profile.ProfileScreen

// ─────────────────────────────────────────────────────────────
// AppNavGraph.kt — Navigation graph toàn app Acuzu
// ─────────────────────────────────────────────────────────────

object AppRoutes {
    const val LOGIN        = "login"
    const val REGISTER     = "register"
    const val HOME         = "home"
    const val CATEGORY     = "category/{categoryId}"
    const val ACCOUNT      = "account/{accountId}"
    const val PERSONAL     = "personal"
    const val EDIT_PROFILE = "edit_profile"

    fun category(id: Int) = "category/$id"
    fun account(id: Int)  = "account/$id"
}

// ── Mock data — thay bằng Room + Repository sau ───────────────
object MockData {
    val mockAccounts = listOf(
        Account(1, "nguyen12112005@gmail.com",        PasswordLevel.LEVEL_3),
        Account(2, "nguyen12112005nguyen@gmail.co...", PasswordLevel.LEVEL_2),
        Account(3, "0329223075",                       PasswordLevel.LEVEL_4),
        Account(4, "nguyen12112005@gmail.com",        PasswordLevel.LEVEL_3),
        Account(5, "nguyen12112005nguyen@gmail.co...", PasswordLevel.LEVEL_1),
        Account(6, "0329223075",                       PasswordLevel.LEVEL_5),
    )

    val categories = listOf(
        Category(1, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
        Category(2, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts.take(3)),
        Category(3, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts.take(2)),
        Category(4, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts.take(4)),
        Category(5, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts.take(2)),
        Category(6, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts),
        Category(7, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts.take(3)),
        Category(8, "Facebook", "https://logo.clearbit.com/facebook.com",  mockAccounts),
        Category(9, "Google",   "https://logo.clearbit.com/google.com",   mockAccounts.take(2)),
    )

    fun getCategoryById(id: Int): Category =
        categories.firstOrNull { it.id == id } ?: categories.first()

    fun getAccountById(id: Int): Account =
        mockAccounts.firstOrNull { it.id == id } ?: mockAccounts.first()

    fun getCategoryNameByAccountId(accountId: Int): String =
        categories.firstOrNull { cat -> cat.accounts.any { it.id == accountId } }?.name ?: "Google"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {

        // ── Login ──────────────────────────────────────────────
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        // ── Register ───────────────────────────────────────────
        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ── Home ───────────────────────────────────────────────
        composable(AppRoutes.HOME) {
            HomeScreen(
                onNavigateToCategory = { id ->
                    navController.navigate(AppRoutes.category(id))
                },
                onNavigateToPersonal = {
                    navController.navigate(AppRoutes.PERSONAL)
                }
            )
        }

        // ── Category Detail ────────────────────────────────────
        composable(
            route     = AppRoutes.CATEGORY,
            arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 1
            val category   = remember(categoryId) { MockData.getCategoryById(categoryId) }

            CategoryDetailScreen(
                category = category,
                onBack   = { navController.popBackStack() },
                onAccountClick = { account ->
                    navController.navigate(AppRoutes.account(account.id))
                }
            )
        }

        // ── Account Detail ─────────────────────────────────────
        composable(
            route     = AppRoutes.ACCOUNT,
            arguments = listOf(navArgument("accountId") { type = NavType.IntType })
        ) { backStackEntry ->
            val accountId    = backStackEntry.arguments?.getInt("accountId") ?: 1
            val account      = remember(accountId) { MockData.getAccountById(accountId) }
            val categoryName = remember(accountId) { MockData.getCategoryNameByAccountId(accountId) }

            AccountDetailScreen(
                account      = account,
                categoryName = categoryName,
                onBack       = { navController.popBackStack() }
            )
        }

        // ── Personal ───────────────────────────────────────────
        composable(AppRoutes.PERSONAL) {
            ProfileScreen(
                onBack        = { navController.popBackStack() },
                onEditProfile = { navController.navigate(AppRoutes.EDIT_PROFILE) },
                onLogout      = {
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Edit Profile ───────────────────────────────────────
        composable(AppRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
    }
}
