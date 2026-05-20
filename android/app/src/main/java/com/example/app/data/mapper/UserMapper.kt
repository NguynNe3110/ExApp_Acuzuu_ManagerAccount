package com.example.app.data.mapper

import com.example.app.data.remote.dto.response.UserDto
import com.example.app.domain.model.User

// ─────────────────────────────────────────────────────────────
// UserMapper.kt — Chuyển đổi UserDto ↔ User domain model
// ─────────────────────────────────────────────────────────────

fun UserDto.toDomain(): User = User(
    id        = id,
    email     = email,
    name      = name,
    avatarUrl = avatar
)
