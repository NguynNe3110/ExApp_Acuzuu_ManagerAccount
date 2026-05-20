package com.example.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// ─────────────────────────────────────────────────────────────
// ServiceProviderEntity.kt — Room Entity (row trong SQLite table)
//
// @Entity: ánh xạ class → table trong SQLite
// @PrimaryKey: cột primary key
// ─────────────────────────────────────────────────────────────

@Entity(tableName = "service_providers")
data class ServiceProviderEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val logoUrl: String,
    val category: String,
    val rating: Double,
    val isFeatured: Boolean,
    val cachedAt: Long = System.currentTimeMillis() // Thời điểm cache
)
