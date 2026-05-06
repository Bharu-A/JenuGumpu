package com.jenugumpu.domain.model

enum class GradeColor { LIGHT, AMBER, DARK }
enum class MoistureLevel { LOW, MEDIUM, HIGH }
enum class FloralSource { COFFEE_BLOSSOM, WILDFLOWER, FOREST_HONEY }

data class HoneyBatch(
    val id: Long = 0L,
    val dateEpochMillis: Long,
    val location: String,
    val quantityKg: Double,
    val floralSource: FloralSource,
    val gradeColor: GradeColor,
    val moistureLevel: MoistureLevel,
    val batchId: String
)

data class DashboardStats(
    val totalStockKg: Double = 0.0,
    val totalBatches: Int = 0,
    val recentBatches: List<HoneyBatch> = emptyList()
)

data class UserProfile(
    val name: String = "",
    val village: String = "",
    val phone: String = "",
    val role: String = "Honey Producer",
    val yearsExperience: String = "",
    val bio: String = "",
    val language: String = "English"
)
