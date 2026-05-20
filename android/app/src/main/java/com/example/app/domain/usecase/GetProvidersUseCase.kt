package com.example.app.domain.usecase

import com.example.app.core.result.Result
import com.example.app.domain.model.ServiceProvider
import com.example.app.domain.repository.ServiceProviderRepository
import javax.inject.Inject

// ─────────────────────────────────────────────────────────────
// GetProvidersUseCase.kt — Business logic layer
//
// UseCase (còn gọi là Interactor) chứa business rule.
// Ví dụ: "Chỉ hiển thị providers có rating >= 4.0"
//        "Sort theo isFeatured trước, rồi mới theo name"
//
// Khi nào cần UseCase?
//   ✅ Có business logic (filter, sort, combine data từ nhiều repo)
//   ❌ Nếu chỉ forward call đến repository → KHÔNG cần UseCase
//      (tránh over-engineering)
// ─────────────────────────────────────────────────────────────

class GetProvidersUseCase @Inject constructor(
    private val repository: ServiceProviderRepository
) {
    /**
     * Lấy providers và sort: featured trước, sau đó theo tên
     * Đây là business rule — không nên đặt trong ViewModel hay Repository
     */
    suspend operator fun invoke(
        page: Int = 1,
        category: String? = null
    ): Result<List<ServiceProvider>> {
        return when (val result = repository.getProviders(page, category)) {
            is Result.Success -> {
                val sorted = result.data
                    .sortedWith(
                        compareByDescending<ServiceProvider> { it.isFeatured }
                            .thenBy { it.name }
                    )
                Result.Success(sorted)
            }
            is Result.Error   -> result
            is Result.Loading -> result
        }
    }
}
