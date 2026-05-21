package com.jenugumpu.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jenugumpu.data.local.entity.HoneyBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HoneyBatchDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(batch: HoneyBatchEntity)

    @Update
    suspend fun update(batch: HoneyBatchEntity)

    @Delete
    suspend fun delete(batch: HoneyBatchEntity)

    @Query("SELECT * FROM honey_batches ORDER BY dateEpochMillis DESC")
    fun observeBatches(): Flow<List<HoneyBatchEntity>>

    @Query("SELECT * FROM honey_batches ORDER BY dateEpochMillis DESC LIMIT 3")
    fun observeRecent(): Flow<List<HoneyBatchEntity>>

    @Query("SELECT COALESCE(SUM(quantityKg),0.0) FROM honey_batches")
    fun observeTotalStock(): Flow<Double>

    @Query("SELECT COUNT(*) FROM honey_batches")
    fun observeBatchCount(): Flow<Int>

    @Query("SELECT COALESCE(MAX(CAST(SUBSTR(batchId, -3) AS INTEGER)), 0) FROM honey_batches WHERE batchId LIKE :prefix")
    suspend fun getMaxSequence(prefix: String): Int
}
