package com.example.app.data.repository

import com.example.app.core.dispatcher.DispatcherProvider
import com.example.app.core.result.Result
import com.example.app.data.mapper.toDomain
import com.example.app.data.remote.api.ServiceProviderApi
import com.example.app.domain.model.ServiceProvider
import com.example.app.domain.repository.ServiceProviderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// ServiceProviderRepositoryImpl.kt — Implementation thực sự
//
// Class này là cầu nối giữa:
//   ↑ Domain layer (chỉ biết interface)
//   ↓ Data layer  (Retrofit, Room, DataStore...)
//
// ViewModel → Repository (interface) → RepositoryImpl → API
// ─────────────────────────────────────────────────────────────

@Singleton
class ServiceProviderRepositoryImpl @Inject constructor(
    private val api: ServiceProviderApi,
    private val dispatchers: DispatcherProvider
) : ServiceProviderRepository {

    override suspend fun getProviders(
        page: Int,
        category: String?
    ): Result<List<ServiceProvider>> {
        // Luôn wrap bằng try-catch để không crash app khi network lỗi
        return try {
            val response = api.getProviders(page = page, category = category)

            if (response.success) {
                // Chuyển List<DTO> → List<DomainModel> qua mapper
                Result.Success(response.data.toDomain())
            } else {
                Result.Error(response.message)
            }
        } catch (e: Exception) {
            // IOException: mất mạng
            // HttpException: server trả về lỗi 4xx/5xx
            Result.Error(e.message ?: "Không thể kết nối đến server")
        }
    }

    override suspend fun getProviderById(id: Int): Result<ServiceProvider> {
        return try {
            val response = api.getProviderById(id)
            if (response.success && response.data != null) {
                Result.Success(response.data.toDomain())
            } else {
                Result.Error(response.message)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Không tìm thấy provider")
        }
    }

    override fun searchProviders(query: String): Flow<Result<List<ServiceProvider>>> =
        flow {
            emit(Result.Loading) // Thông báo đang tải

            try {
                val response = api.searchProviders(query)
                if (response.success) {
                    emit(Result.Success(response.data.toDomain()))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message ?: "Lỗi tìm kiếm"))
            }
        }.flowOn(dispatchers.io) // Chạy trên IO thread, không block Main thread
}
