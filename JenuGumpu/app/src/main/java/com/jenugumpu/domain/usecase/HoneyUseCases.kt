package com.jenugumpu.domain.usecase

import com.jenugumpu.domain.model.DashboardStats
import com.jenugumpu.domain.model.HoneyBatch
import com.jenugumpu.domain.repository.HoneyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddHoneyBatchUseCase @Inject constructor(private val repository: HoneyRepository) {
    suspend operator fun invoke(batch: HoneyBatch) = repository.addBatch(batch)
}

class DeleteHoneyBatchUseCase @Inject constructor(private val repository: HoneyRepository) {
    suspend operator fun invoke(batch: HoneyBatch) = repository.deleteBatch(batch)
}

class GetBatchesUseCase @Inject constructor(private val repository: HoneyRepository) {
    operator fun invoke(): Flow<List<HoneyBatch>> = repository.observeBatches()
}

class GetDashboardUseCase @Inject constructor(private val repository: HoneyRepository) {
    operator fun invoke(): Flow<DashboardStats> = repository.observeDashboard()
}

class GenerateBatchIdUseCase @Inject constructor(private val repository: HoneyRepository) {
    suspend operator fun invoke(date: java.time.LocalDate): String = repository.generateBatchId(date)
}

class CalculateProfitUseCase @Inject constructor() {
    operator fun invoke(quantityKg: Double, costPerKg: Double, sellPerKg: Double): Double {
        return (sellPerKg - costPerKg) * quantityKg
    }
}
