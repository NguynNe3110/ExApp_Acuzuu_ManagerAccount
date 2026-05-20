package com.example.app.domain.model

// ─────────────────────────────────────────────────────────────
// Account.kt — Domain models cho app quản lý tài khoản Acuzu
// ─────────────────────────────────────────────────────────────

/** Mức độ mật khẩu từ 1 (yếu) đến 5 (mạnh) */
enum class PasswordLevel(val label: String, val value: Int) {
    LEVEL_1("Mức 1", 1),
    LEVEL_2("Mức 2", 2),
    LEVEL_3("Mức 3", 3),
    LEVEL_4("Mức 4", 4),
    LEVEL_5("Mức 5", 5);

    companion object {
        fun fromValue(v: Int) = entries.firstOrNull { it.value == v } ?: LEVEL_3
    }
}

/** Loại tài khoản — hiển thị trong dropdown "Loại" */
enum class AccountType(val label: String) {
    GOOGLE("Google"),
    FACEBOOK("Facebook"),
    EMAIL("Email"),
    BANK("Ngân hàng"),
    OTHER("Khác")
}

/** Một field tùy chỉnh trên màn hình Account Detail */
data class CustomField(
    val id: Int,
    val label: String,    // vd: "Username", "Xác thực 2 lớp", "Số điện thoại"
    val value: String
)

/** Một tài khoản trong danh sách */
data class Account(
    val id: Int,
    val displayName: String,       // email hoặc tên hiển thị (bị truncate trên list)
    val passwordLevel: PasswordLevel,
    val customFields: List<CustomField> = emptyList(),
    val notes: String = ""
)

/** Một thư mục / danh mục chứa các tài khoản */
data class Category(
    val id: Int,
    val name: String,              // vd: "Google", "Facebook"
    val logoUrl: String?,          // URL hoặc null → dùng icon mặc định
    val accounts: List<Account> = emptyList(),
    val subCategories: List<Category> = emptyList()
)
