package com.jenugumpu.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jenugumpu.domain.model.FloralSource
import com.jenugumpu.domain.model.GradeColor
import com.jenugumpu.domain.model.HoneyBatch
import com.jenugumpu.domain.model.MoistureLevel
import com.jenugumpu.ui.components.AppBackground
import com.jenugumpu.ui.components.AppHeader
import com.jenugumpu.ui.components.EducationCard
import com.jenugumpu.ui.components.GradeRow
import com.jenugumpu.ui.components.HoneyBatchCard
import com.jenugumpu.ui.components.MarketInsightCard
import com.jenugumpu.ui.components.MoistureRow
import com.jenugumpu.ui.components.SelectChip
import com.jenugumpu.ui.components.StatCard
import com.jenugumpu.viewmodel.BatchListViewModel
import com.jenugumpu.viewmodel.DashboardViewModel
import com.jenugumpu.viewmodel.HarvestViewModel
import com.jenugumpu.viewmodel.ProfitViewModel
import com.jenugumpu.viewmodel.SettingsViewModel
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HarvestScreen(vm: HarvestViewModel) {
    val state by vm.ui.collectAsState()
    val context = LocalContext.current
    val snack = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(state.message) { state.message?.let { snack.showSnackbar(it); vm.clearMessage() } }

    AppBackground {
        Scaffold(snackbarHost = { SnackbarHost(hostState = snack) }) { pad ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(pad),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppHeader()
                }
                item {
                    Text("Basic Info", style = MaterialTheme.typography.titleLarge)
                }
                item {
                    Button(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> vm.onDateChange(LocalDate.of(y, m + 1, d)) },
                            state.date.year,
                            state.date.monthValue - 1,
                            state.date.dayOfMonth
                        ).show()
                    }) { Text("Date: ${state.date}") }
                }
                item {
                    OutlinedTextField(state.location, vm::onLocation, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(state.quantity, vm::onQuantity, label = { Text("Quantity (kg)") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = state.floralSource.name.replace("_", " Honey"),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Floral Source") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            FloralSource.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.name.replace("_", " Honey")) },
                                    onClick = { vm.onFloral(it); expanded = false }
                                )
                            }
                        }
                    }
                }
                item {
                    Text("Only natural honey sources allowed", color = Color.Gray)
                }
                item {
                    Text("Quality", style = MaterialTheme.typography.titleLarge)
                }
                item {
                    GradeRow(state.gradeColor, vm::onGrade)
                }
                item {
                    MoistureRow(state.moistureLevel, vm::onMoisture)
                }
                item {
                    Text("Batch ID: ${state.batchId}")
                }
                item {
                    Button(onClick = vm::save, modifier = Modifier.fillMaxWidth()) { Text("Save Harvest") }
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(vm: DashboardViewModel, padding: PaddingValues = PaddingValues(16.dp)) {
    val s by vm.ui.collectAsState()
    val retail by vm.marketPrice.collectAsState()
    val wholesale by vm.wholesalePrice.collectAsState()
    AppBackground {
        LazyColumn(Modifier.fillMaxSize().padding(padding), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                AppHeader()
            }
            item {
                StatCard("Total Stock", "${"%.2f".format(s.totalStockKg)} kg")
            }
            item {
                StatCard("Total Batches", "${s.totalBatches}")
            }
            item { MarketInsightCard(retail = retail, wholesale = wholesale) }
            item { EducationCard() }
            item { Text("Recent 3 Entries") }
            items(s.recentBatches) { HoneyBatchCard(it) }
        }
    }
}

@Composable
fun BatchListScreen(vm: BatchListViewModel) {
    val itemsState by vm.batches.collectAsState()
    val editingBatch by vm.editingBatch.collectAsState()

    AppBackground {
        LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Text("All Honey Batches", style = MaterialTheme.typography.headlineMedium)
            }
            if (itemsState.isEmpty()) {
                item {
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No batches yet")
                        Text("Start by adding a harvest")
                    }
                }
            } else {
                items(itemsState) { batch -> 
                    HoneyBatchCard(
                        batch = batch,
                        onEdit = { vm.setEditingBatch(batch) },
                        onDelete = { vm.deleteBatch(batch) }
                    ) 
                }
            }
            item { EducationCard() }
        }

        if (editingBatch != null) {
            EditBatchDialog(
                batch = editingBatch!!,
                onDismiss = { vm.setEditingBatch(null) },
                onSave = { updated -> vm.updateBatch(updated) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBatchDialog(
    batch: HoneyBatch,
    onDismiss: () -> Unit,
    onSave: (HoneyBatch) -> Unit
) {
    val context = LocalContext.current
    var date by remember { mutableStateOf(LocalDate.ofEpochDay(batch.dateEpochMillis / 86400000)) }
    var location by remember { mutableStateOf(batch.location) }
    var quantity by remember { mutableStateOf(batch.quantityKg.toString()) }
    var floral by remember { mutableStateOf(batch.floralSource) }
    var grade by remember { mutableStateOf(batch.gradeColor) }
    var moisture by remember { mutableStateOf(batch.moistureLevel) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Batch ${batch.batchId}") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Button(onClick = {
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> date = LocalDate.of(y, m + 1, d) },
                            date.year,
                            date.monthValue - 1,
                            date.dayOfMonth
                        ).show()
                    }, modifier = Modifier.fillMaxWidth()) { Text("Date: $date") }
                }
                item {
                    OutlinedTextField(location, { location = it }, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    OutlinedTextField(quantity, { quantity = it }, label = { Text("Quantity (kg)") }, modifier = Modifier.fillMaxWidth())
                }
                item {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = floral.name.replace("_", " Honey"),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Floral Source") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            FloralSource.entries.forEach {
                                DropdownMenuItem(
                                    text = { Text(it.name.replace("_", " Honey")) },
                                    onClick = { floral = it; expanded = false }
                                )
                            }
                        }
                    }
                }
                item { Text("Quality", style = MaterialTheme.typography.titleMedium) }
                item { GradeRow(grade) { grade = it } }
                item { MoistureRow(moisture) { moisture = it } }
            }
        },
        confirmButton = {
            Button(onClick = {
                val q = quantity.toDoubleOrNull()
                if (q != null && q > 0 && location.isNotBlank()) {
                    onSave(
                        batch.copy(
                            dateEpochMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                            location = location.trim(),
                            quantityKg = q,
                            floralSource = floral,
                            gradeColor = grade,
                            moistureLevel = moisture
                        )
                    )
                }
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ProfitScreen(vm: ProfitViewModel) {
    val result by vm.result.collectAsState()
    var costInput by remember { mutableStateOf("0") }
    var sellInput by remember { mutableStateOf("0") }
    AppBackground {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AppHeader()
            OutlinedTextField(costInput,
                onValueChange = {
                    costInput = it
                    vm.onCost(it)
                },
                label = { Text("Cost per kg") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(sellInput,
                onValueChange = {
                    sellInput = it
                    vm.onSell(it)
                },
                label = { Text("Selling per kg") },
                modifier = Modifier.fillMaxWidth()
            )
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Earnings Summary", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Profit/kg: ₹${"%.2f".format(result.first)}")
                    Text("Total Profit: ₹${"%.2f".format(result.second)}")
                    Spacer(Modifier.height(8.dp))
                    Text("Tip: Branding increases value by 30–50%")
                    Text(vm.getSuggestion())
                }
            }
        }
    }
}

// ── Profile Screen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: SettingsViewModel) {
    // Saved profile from ViewModel (only updated on explicit Save)
    val savedProfile by vm.profile.collectAsState()
    val profileSaved by vm.profileSaved.collectAsState()
    val snack = remember { SnackbarHostState() }

    // Local draft state — what the user is currently editing
    var draftName       by rememberSaveable { mutableStateOf(savedProfile.name) }
    var draftPhone      by rememberSaveable { mutableStateOf(savedProfile.phone) }
    var draftVillage    by rememberSaveable { mutableStateOf(savedProfile.village) }
    var draftRole       by rememberSaveable { mutableStateOf(savedProfile.role) }
    var draftExperience by rememberSaveable { mutableStateOf(savedProfile.yearsExperience) }
    var draftBio        by rememberSaveable { mutableStateOf(savedProfile.bio) }

    // Sync draft when saved profile changes (e.g. first load from prefs)
    LaunchedEffect(savedProfile) {
        draftName       = savedProfile.name
        draftPhone      = savedProfile.phone
        draftVillage    = savedProfile.village
        draftRole       = savedProfile.role
        draftExperience = savedProfile.yearsExperience
        draftBio        = savedProfile.bio
    }

    LaunchedEffect(profileSaved) {
        if (profileSaved) snack.showSnackbar("Profile saved successfully ✓")
    }

    Scaffold(snackbarHost = { SnackbarHost(snack) }) { pad ->
        AppBackground {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // ── Profile Banner (shows persisted/saved data) ─────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = savedProfile.name.take(2).uppercase().ifBlank { "JG" },
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = savedProfile.name.ifBlank { "Your Name" },
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = savedProfile.role,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            if (savedProfile.village.isNotBlank()) {
                                Spacer(Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = savedProfile.village,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }

                // ── Section: Personal Information ───────────────────────────
                item { SettingsSectionHeader("Personal Information") }

                item {
                    SettingsCard {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = draftName,
                                onValueChange = { draftName = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = draftPhone,
                                onValueChange = { draftPhone = it },
                                label = { Text("Phone Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = draftVillage,
                                onValueChange = { draftVillage = it },
                                label = { Text("Village / Location") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = draftRole,
                                onValueChange = { draftRole = it },
                                label = { Text("Role / Occupation") },
                                leadingIcon = { Icon(Icons.Default.Settings, null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = draftExperience,
                                onValueChange = { draftExperience = it },
                                label = { Text("Years of Experience") },
                                leadingIcon = { Icon(Icons.Default.Star, null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = draftBio,
                                onValueChange = { draftBio = it },
                                label = { Text("About Me") },
                                leadingIcon = { Icon(Icons.Default.Edit, null) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 4
                            )
                        }
                    }
                }

                // ── Save Profile Button ─────────────────────────────────────
                item {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // Commit draft to ViewModel first, then persist
                            vm.setName(draftName)
                            vm.setPhone(draftPhone)
                            vm.setVillage(draftVillage)
                            vm.setRole(draftRole)
                            vm.setExperience(draftExperience)
                            vm.setBio(draftBio)
                            vm.saveProfile()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(52.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Save Profile", style = MaterialTheme.typography.titleMedium)
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

// ── Settings Screen (Preferences & About only) ──────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(vm: SettingsViewModel, onToggleLocale: (Boolean) -> Unit) {
    val kn by vm.isKannada.collectAsState()
    val isDark by vm.isDarkMode.collectAsState()
    var notificationsEnabled by remember { mutableStateOf(true) }

    AppBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {

            // ── Section: Preferences ────────────────────────────────────────
            item { SettingsSectionHeader("Preferences") }

            item {
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {

                        // Dark Mode
                        SettingsToggleRow(
                            icon = if (isDark) Icons.Default.Star else Icons.Default.Edit,
                            title = if (isDark) "Dark Mode" else "Light Mode",
                            subtitle = "Switch app appearance",
                            checked = isDark,
                            onCheckedChange = { vm.setDarkMode(it) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Notifications
                        SettingsToggleRow(
                            icon = Icons.Default.Notifications,
                            title = "Notifications",
                            subtitle = "Harvest reminders & market tips",
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        // Language
                        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Column(Modifier.weight(1f)) {
                                    Text("Language", style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "Current: ${if (kn) "ಕನ್ನಡ" else "English"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(start = 36.dp)
                            ) {
                                SelectChip("English", !kn) {
                                    vm.setKannada(false)
                                    onToggleLocale(false)
                                }
                                SelectChip("ಕನ್ನಡ", kn) {
                                    vm.setKannada(true)
                                    onToggleLocale(true)
                                }
                            }
                        }
                    }
                }
            }

            // ── Section: About ──────────────────────────────────────────────
            item { SettingsSectionHeader("About") }

            item {
                SettingsCard {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        SettingsInfoRow(Icons.Default.Info, "App Name", "Jenu-Gumpu")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        SettingsInfoRow(Icons.Default.Build, "Version", "1.0.0")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        SettingsInfoRow(Icons.Default.Person, "For", "Honey Producer Collectives")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                        SettingsInfoRow(Icons.Default.Info, "Multi-lingual", "English & Kannada")
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "🍯 About Jenu-Gumpu",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("Helps honey producers sell directly and earn more.")
                        Text("Supports rural farmers with Kannada-ready labels.")
                        Text("Built with ❤ for sustainable beekeeping communities.")
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}


// ── Reusable setting sub-composables ────────────────────────────────────────

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 6.dp)
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
