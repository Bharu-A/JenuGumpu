package com.jenugumpu.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.R
import com.jenugumpu.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SelectChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.large,
        tonalElevation = if (selected) 4.dp else 0.dp,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GradeRow(selected: GradeColor, onSelect: (GradeColor) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GradeColor.entries.forEach {
            SelectChip(it.name, selected == it) { onSelect(it) }
        }
    }
}

@Composable
fun MoistureRow(selected: MoistureLevel, onSelect: (MoistureLevel) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MoistureLevel.entries.forEach {
            SelectChip(it.name, selected == it) { onSelect(it) }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun MarketInsightCard(retail: Double = 450.0, wholesale: Double = 280.0) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painter = painterResource(R.drawable.honey_icon), contentDescription = null)
                Text("Market Insight", fontWeight = FontWeight.Bold)
            }
            Text("Retail: ₹${"%.0f".format(retail)}/kg")
            Text("Wholesale: ₹${"%.0f".format(wholesale)}/kg")
            Text("Tip: Selling as branded increases profit by 40%")
        }
    }
}

@Composable
fun EducationCard() {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(painter = painterResource(R.drawable.bee_icon), contentDescription = null)
                Text("Sustainable Harvest", fontWeight = FontWeight.Bold)
            }
            Text("✔ Do not burn hives")
            Text("✔ Leave 30% honey for bees")
            Text("✔ Harvest in season only")
        }
    }
}

@Composable
fun HoneyBatchCard(batch: HoneyBatch, onDelete: (() -> Unit)? = null) {
    val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(batch.dateEpochMillis))

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(batch.batchId, style = MaterialTheme.typography.titleMedium)
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Batch",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Text("$date • ${batch.location}")

            Spacer(Modifier.height(8.dp))
            Text("${batch.quantityKg} kg • ${batch.floralSource.name}")

            Spacer(Modifier.height(8.dp))
            Text("${batch.gradeColor.name} • ${batch.moistureLevel.name}",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
