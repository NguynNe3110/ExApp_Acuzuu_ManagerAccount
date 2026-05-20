package com.example.app.data.local.dao

import androidx.room.*
import com.example.app.data.local.entity.ServiceProviderEntity
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────
// ServiceProviderDao.kt — Data Access Object
//
// DAO định nghĩa các câu SQL query.
// Room generate implementation tự động từ annotation.
// Không cần viết SQL thủ công cho các thao tác cơ bản!
// ─────────────────────────────────────────────────────────────

@Dao
interface ServiceProviderDao {

    // Flow<List<...>>: tự động emit lại khi data trong DB thay đổi
    @Query("SELECT * FROM service_providers ORDER BY isFeatured DESC, name ASC")
    fun getAllProviders(): Flow<List<ServiceProviderEntity>>

    @Query("SELECT * FROM service_providers WHERE id = :id")
    suspend fun getProviderById(id: Int): ServiceProviderEntity?

    // REPLACE: nếu đã có record với id này → overwrite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(providers: List<ServiceProviderEntity>)

    @Query("DELETE FROM service_providers")
    suspend fun clearAll()

    // Xóa cache cũ hơn 1 giờ (3600000ms)
    @Query("DELETE FROM service_providers WHERE cachedAt < :threshold")
    suspend fun clearOldCache(threshold: Long = System.currentTimeMillis() - 3_600_000)
}
