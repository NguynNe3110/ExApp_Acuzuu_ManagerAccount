package com.example.app.core.di

import com.example.app.core.dispatcher.DefaultDispatcherProvider
import com.example.app.core.dispatcher.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// AppModule.kt — Các dependency chung của toàn app
// ─────────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        impl: DefaultDispatcherProvider
    ): DispatcherProvider
}
