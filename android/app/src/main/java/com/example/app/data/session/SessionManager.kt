package com.example.app.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// SessionManager.kt — Lưu và đọc token của người dùng
//
// Dùng DataStore thay vì SharedPreferences vì:
//   → Thread-safe (không crash khi đọc/ghi từ nhiều thread)
//   → Hỗ trợ coroutines & Flow
//   → Google khuyến nghị từ Android 6.0+
// ─────────────────────────────────────────────────────────────

// Khởi tạo DataStore ở file-level (chỉ tạo 1 lần)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val KEY_ACCESS_TOKEN  = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    // ── Lưu token ─────────────────────────────────────────────

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN]  = accessToken
            prefs[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    // ── Đọc token (dạng Flow để observe realtime) ─────────────

    val accessTokenFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_ACCESS_TOKEN] }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> !prefs[KEY_ACCESS_TOKEN].isNullOrBlank() }

    // ── Đọc token đồng bộ (chỉ dùng trong Interceptor) ────────
    // runBlocking được chấp nhận ở đây vì Interceptor không thể suspend

    fun getAccessTokenBlocking(): String? = runBlocking {
        context.dataStore.data.firstOrNull()?.get(KEY_ACCESS_TOKEN)
    }

    // ── Xóa token khi logout ───────────────────────────────────

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
