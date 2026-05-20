package com.example.app.data.remote.api

import com.example.app.data.remote.dto.BaseResponseDto
import com.example.app.data.remote.dto.PaginatedResponseDto
import com.example.app.data.remote.dto.response.ServiceProviderDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ─────────────────────────────────────────────────────────────
// ServiceProviderApi.kt — Interface định nghĩa các API endpoint
//
// Retrofit sẽ tự generate implementation cho interface này.
// Chỉ cần khai báo endpoint, Retrofit lo phần còn lại.
// ─────────────────────────────────────────────────────────────

interface ServiceProviderApi {

    /**
     * Lấy danh sách service providers có phân trang
     * GET /api/v1/providers?page=1&pageSize=20&category=all
     */
    @GET("api/v1/providers")
    suspend fun getProviders(
        @Query("page")     page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("category") category: String? = null
    ): PaginatedResponseDto<ServiceProviderDto>

    /**
     * Lấy chi tiết một provider theo id
     * GET /api/v1/providers/{id}
     */
    @GET("api/v1/providers/{id}")
    suspend fun getProviderById(
        @Path("id") id: Int
    ): BaseResponseDto<ServiceProviderDto>

    /**
     * Tìm kiếm providers theo tên
     * GET /api/v1/providers/search?q=google
     */
    @GET("api/v1/providers/search")
    suspend fun searchProviders(
        @Query("q") query: String
    ): PaginatedResponseDto<ServiceProviderDto>
}
