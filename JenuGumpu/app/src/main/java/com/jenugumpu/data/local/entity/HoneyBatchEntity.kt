package com.jenugumpu.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "honey_batches",
    indices = [Index(value = ["batchId"], unique = true)]
)
data class HoneyBatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val dateEpochMillis: Long,
    val location: String,
    val quantityKg: Double,
    val floralSource: String,
    val gradeColor: String,
    val moistureLevel: String,
    val batchId: String
)
