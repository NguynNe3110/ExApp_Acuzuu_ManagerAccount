package com.example.app.domain.repository

import com.example.app.core.result.Result
import com.example.app.domain.model.ServiceProvider
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────
// ServiceProviderRepository.kt — Interface (domain layer)
//
// Tại sao domain layer chỉ có interface?
//   → Domain không quan tâm data đến từ đâu (API, DB, cache...)
//   → Dễ swap implementation: hôm nay dùng API, mai dùng Room
//   → Dễ test: mock interface thay vì mock Retrofit
//
// Nguyên tắc: Dependency Inversion (SOLID)
//   High-level (ViewModel) phụ thuộc vào abstraction (interface)
//   KHÔNG phụ thuộc vào implementation cụ thể (RepositoryImpl)
// ─────────────────────────────────────────────────────────────

interface ServiceProviderRepository {

    /** Lấy danh sách providers từ network hoặc cache */
    suspend fun getProviders(
        page: Int = 1,
        category: String? = null
    ): Result<List<ServiceProvider>>

    /** Lấy chi tiết một provider */
    suspend fun getProviderById(id: Int): Result<ServiceProvider>

    /** Tìm kiếm providers theo tên */
    fun searchProviders(query: String): Flow<Result<List<ServiceProvider>>>
}
