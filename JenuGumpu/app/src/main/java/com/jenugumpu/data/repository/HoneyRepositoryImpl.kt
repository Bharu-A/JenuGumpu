package com.jenugumpu.data.repository

import com.jenugumpu.data.local.dao.HoneyBatchDao
import com.jenugumpu.data.local.entity.HoneyBatchEntity
import com.jenugumpu.domain.model.DashboardStats
import com.jenugumpu.domain.model.FloralSource
import com.jenugumpu.domain.model.GradeColor
import com.jenugumpu.domain.model.HoneyBatch
import com.jenugumpu.domain.model.MoistureLevel
import com.jenugumpu.domain.repository.HoneyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HoneyRepositoryImpl @Inject constructor(
    private val dao: HoneyBatchDao
) : HoneyRepository {

    override suspend fun addBatch(batch: HoneyBatch) {
        dao.insert(batch.toEntity())
    }

    override suspend fun updateBatch(batch: HoneyBatch) {
        dao.update(batch.toEntity())
    }

    override suspend fun deleteBatch(batch: HoneyBatch) {
        dao.delete(batch.toEntity())
    }

    override fun observeBatches(): Flow<List<HoneyBatch>> = dao.observeBatches().map { rows ->
        rows.map { it.toDomain() }
    }

    override fun observeDashboard(): Flow<DashboardStats> = combine(
        dao.observeTotalStock(),
        dao.observeBatchCount(),
        dao.observeRecent()
    ) { stock, count, recent ->
        DashboardStats(stock, count, recent.map { it.toDomain() })
    }

    override suspend fun generateBatchId(date: LocalDate): String {
        val year = date.year
        val prefix = "HNY-$year-%"
        val next = dao.getMaxSequence(prefix) + 1
        return "HNY-$year-${next.toString().padStart(3, '0')}"
    }

    private fun HoneyBatch.toEntity() = HoneyBatchEntity(
        id = id,
        dateEpochMillis = dateEpochMillis,
        location = location,
        quantityKg = quantityKg,
        floralSource = floralSource.name,
        gradeColor = gradeColor.name,
        moistureLevel = moistureLevel.name,
        batchId = batchId
    )

    private fun HoneyBatchEntity.toDomain() = HoneyBatch(
        id = id,
        dateEpochMillis = dateEpochMillis,
        location = location,
        quantityKg = quantityKg,
        floralSource = FloralSource.valueOf(floralSource),
        gradeColor = GradeColor.valueOf(gradeColor),
        moistureLevel = MoistureLevel.valueOf(moistureLevel),
        batchId = batchId
    )
}
