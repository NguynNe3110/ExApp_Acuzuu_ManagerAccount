package com.example.app.data.mapper

import com.example.app.data.remote.dto.response.ServiceProviderDto
import com.example.app.domain.model.ServiceProvider

// ─────────────────────────────────────────────────────────────
// ServiceProviderMapper.kt — Chuyển đổi DTO ↔ Domain Model
//
// Tại sao cần Mapper?
//   → DTO phụ thuộc Retrofit/JSON, không dùng được trong domain
//   → Domain model là "sạch", không biết đến network layer
//   → Mapper là cầu nối giữa hai thế giới đó
//
// Flow: API trả về DTO → Mapper chuyển sang Model → ViewModel dùng
// ─────────────────────────────────────────────────────────────

// Chuyển 1 DTO thành 1 Model
fun ServiceProviderDto.toDomain(): ServiceProvider = ServiceProvider(
    id         = id,
    name       = name,
    logoUrl    = logoUrl,
    category   = category,
    rating     = rating,
    isFeatured = isFeatured
)

// Chuyển danh sách DTO thành danh sách Model
fun List<ServiceProviderDto>.toDomain(): List<ServiceProvider> =
    map { it.toDomain() }
