package com.example.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// ─────────────────────────────────────────────────────────────
// BaseResponseDto.kt — Wrapper chung cho mọi response từ server
//
// Hầu hết API đều có dạng:
// {
//   "success": true,
//   "message": "OK",
//   "data": { ... }       ← T là kiểu dữ liệu thực sự
// }
// ─────────────────────────────────────────────────────────────

data class BaseResponseDto<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: T?
)

// Dùng cho response có danh sách + phân trang
data class PaginatedResponseDto<T>(
    @SerializedName("success")    val success: Boolean,
    @SerializedName("message")    val message: String,
    @SerializedName("data")       val data: List<T>,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("page")       val page: Int,
    @SerializedName("pageSize")   val pageSize: Int
)
