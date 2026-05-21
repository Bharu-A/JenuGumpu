package com.jenugumpu.domain.repository

import com.jenugumpu.domain.model.DashboardStats
import com.jenugumpu.domain.model.HoneyBatch
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HoneyRepository {
    suspend fun addBatch(batch: HoneyBatch)
    suspend fun updateBatch(batch: HoneyBatch)
    suspend fun deleteBatch(batch: HoneyBatch)
    fun observeBatches(): Flow<List<HoneyBatch>>
    fun observeDashboard(): Flow<DashboardStats>
    suspend fun generateBatchId(date: LocalDate): String
}
