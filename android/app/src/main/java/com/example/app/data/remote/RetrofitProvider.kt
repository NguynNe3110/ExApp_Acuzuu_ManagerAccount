package com.example.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import com.example.app.BuildConfig

// ─────────────────────────────────────────────────────────────
// RetrofitProvider.kt — Cấu hình Retrofit + OkHttpClient
//
// Singleton: chỉ tạo 1 instance duy nhất trong toàn app.
// Hilt sẽ inject class này vào bất kỳ đâu cần dùng.
// ─────────────────────────────────────────────────────────────

@Singleton
class RetrofitProvider @Inject constructor(
    private val authInterceptor: AuthInterceptor
) {
    companion object {
        private const val BASE_URL     = "https://api.acuzu.com/"
        private const val TIMEOUT_SECS = 30L
    }

    // OkHttpClient: tầng transport của HTTP
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)            // Tự đính token
            .addInterceptor(loggingInterceptor())       // Log request/response
            .connectTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECS, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance chính
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Tạo API service từ interface
    inline fun <reified T> createApi(): T = retrofit.create(T::class.java)

    // Log network request khi debug, tắt khi release
    private fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY  // Log đầy đủ body khi debug
        } else {
            HttpLoggingInterceptor.Level.NONE  // Không log khi production
        }
    }
}
