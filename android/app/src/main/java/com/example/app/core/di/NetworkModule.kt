package com.example.app.core.di

import com.example.app.data.remote.RetrofitProvider
import com.example.app.data.remote.api.AuthApi
import com.example.app.data.remote.api.ServiceProviderApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// NetworkModule.kt — Cung cấp các dependency liên quan đến network
//
// @Module: nơi Hilt "học" cách tạo các dependency
// @InstallIn(SingletonComponent): sống cùng Application
//   → Retrofit, OkHttpClient chỉ tạo 1 lần duy nhất
//
// Hilt sẽ tự inject những gì bạn khai báo ở đây
// vào bất kỳ class nào có @Inject
// ─────────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideServiceProviderApi(retrofitProvider: RetrofitProvider): ServiceProviderApi =
        retrofitProvider.createApi()

    @Provides
    @Singleton
    fun provideAuthApi(retrofitProvider: RetrofitProvider): AuthApi =
        retrofitProvider.createApi()
}
