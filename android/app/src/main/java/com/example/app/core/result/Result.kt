package com.example.app.core.result

// ─────────────────────────────────────────────────────────────
// Result.kt — Wrapper chung cho mọi kết quả từ network/DB
//
// Tại sao dùng sealed class?
//   → Compiler biết chính xác có bao nhiêu trạng thái
//   → Bắt buộc xử lý hết case khi dùng `when`
//   → Không bao giờ quên handle lỗi
// ─────────────────────────────────────────────────────────────

sealed class Result<out T> {
    /** Gọi API thành công, data chứa dữ liệu trả về */
    data class Success<T>(val data: T) : Result<T>()

    /** Có lỗi xảy ra, message mô tả lỗi */
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()

    /** Đang tải, dùng để hiển thị loading UI */
    data object Loading : Result<Nothing>()
}

// Extension function — dùng cho các trường hợp cần transform data
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error   -> this
    is Result.Loading -> this
}

// Extension function — xử lý kết quả gọn hơn
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (String, Int?) -> Unit): Result<T> {
    if (this is Result.Error) action(message, code)
    return this
}
