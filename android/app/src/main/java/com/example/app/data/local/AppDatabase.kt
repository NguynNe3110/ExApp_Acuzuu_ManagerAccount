package com.example.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app.data.local.entity.ServiceProviderEntity

// ─────────────────────────────────────────────────────────────
// AppDatabase.kt — Room Database
//
// Room là ORM (Object-Relational Mapping) cho SQLite trên Android
// Dùng để cache data offline
//
// Khi nào cần Room?
//   ✅ App cần hoạt động offline
//   ✅ Cache danh sách để load nhanh hơn
//   ❌ App đơn giản, luôn cần mạng → dùng Retrofit trực tiếp
// ─────────────────────────────────────────────────────────────

@Database(
    entities = [ServiceProviderEntity::class],
    version = 1,
    exportSchema = false // Tắt schema export khi học, bật lên khi production
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceProviderDao(): com.example.app.data.local.dao.ServiceProviderDao
}
