package com.example.app.data.remote.dto.response

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────
// ServiceProviderDto.kt — DTO (Data Transfer Object)
//
// DTO chỉ là "túi chứa data" từ API.
// KHÔNG thêm logic vào đây.
// Tên field theo JSON của server, dùng @SerializedName để map.
// ─────────────────────────────────────────────────────────────

data class ServiceProviderDto(
    @SerializedName("id")          val id: Int,
    @SerializedName("name")        val name: String,
    @SerializedName("logo_url")    val logoUrl: String,
    @SerializedName("category")    val category: String,
    @SerializedName("rating")      val rating: Double,
    @SerializedName("is_featured") val isFeatured: Boolean
)
