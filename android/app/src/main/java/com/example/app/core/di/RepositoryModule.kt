package com.example.app.core.di

import com.example.app.data.repository.AuthRepositoryImpl
import com.example.app.data.repository.ServiceProviderRepositoryImpl
import com.example.app.domain.repository.AuthRepository
import com.example.app.domain.repository.ServiceProviderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// RepositoryModule.kt — Bind interface → implementation
//
// @Binds: hiệu quả hơn @Provides khi chỉ cần map interface → class
// Hilt biết: "Khi ai cần ServiceProviderRepository → inject ServiceProviderRepositoryImpl"
//
// Đây là nơi "nối dây" giữa domain (interface) và data (implementation)
// ─────────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindServiceProviderRepository(
        impl: ServiceProviderRepositoryImpl
    ): ServiceProviderRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
