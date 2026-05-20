package com.example.app.domain.model

// ─────────────────────────────────────────────────────────────
// ServiceProvider.kt — Domain Model (pure Kotlin, không import Android)
//
// Domain model là "ngôn ngữ" của business logic.
// KHÔNG phụ thuộc vào Retrofit, Room, hay bất kỳ framework nào.
// Tên field theo chuẩn Kotlin (camelCase), không phụ thuộc JSON.
//
// Khác biệt với DTO:
//   DTO  → tên field theo JSON server (logo_url, is_featured)
//   Model → tên field theo Kotlin convention (logoUrl, isFeatured)
// ─────────────────────────────────────────────────────────────

data class ServiceProvider(
    val id: Int,
    val name: String,
    val logoUrl: String,
    val category: String,
    val rating: Double,
    val isFeatured: Boolean
)
