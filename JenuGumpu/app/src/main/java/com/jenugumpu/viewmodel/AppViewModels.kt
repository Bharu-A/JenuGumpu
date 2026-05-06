package com.jenugumpu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenugumpu.domain.model.FloralSource
import com.jenugumpu.domain.model.GradeColor
import com.jenugumpu.domain.model.HoneyBatch
import com.jenugumpu.domain.model.MoistureLevel
import com.jenugumpu.domain.model.UserProfile
import com.jenugumpu.domain.usecase.AddHoneyBatchUseCase
import com.jenugumpu.domain.usecase.CalculateProfitUseCase
import com.jenugumpu.domain.usecase.DeleteHoneyBatchUseCase
import com.jenugumpu.domain.usecase.GenerateBatchIdUseCase
import com.jenugumpu.domain.usecase.GetBatchesUseCase
import com.jenugumpu.domain.usecase.GetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class HarvestUiState(
    val date: LocalDate = LocalDate.now(),
    val location: String = "",
    val quantity: String = "",
    val floralSource: FloralSource = FloralSource.COFFEE_BLOSSOM,
    val gradeColor: GradeColor = GradeColor.LIGHT,
    val moistureLevel: MoistureLevel = MoistureLevel.LOW,
    val batchId: String = "",
    val isLoading: Boolean = false,
    val message: String? = null
)

@HiltViewModel
class HarvestViewModel @Inject constructor(
    private val addUseCase: AddHoneyBatchUseCase,
    private val generateBatchIdUseCase: GenerateBatchIdUseCase
) : ViewModel() {
    private val _ui = MutableStateFlow(HarvestUiState())
    val ui: StateFlow<HarvestUiState> = _ui.asStateFlow()

    init {
        refreshBatchId()
    }

    fun onDateChange(d: LocalDate) {
        _ui.update { it.copy(date = d) }
        refreshBatchId()
    }
    fun onLocation(v: String) { _ui.update { it.copy(location = v) } }
    fun onQuantity(v: String) { _ui.update { it.copy(quantity = v) } }
    fun onFloral(v: FloralSource) { _ui.update { it.copy(floralSource = v) } }
    fun onGrade(v: GradeColor) { _ui.update { it.copy(gradeColor = v) } }
    fun onMoisture(v: MoistureLevel) { _ui.update { it.copy(moistureLevel = v) } }
    fun clearMessage() { _ui.update { it.copy(message = null) } }

    private fun refreshBatchId() = viewModelScope.launch {
        val id = generateBatchIdUseCase(_ui.value.date)
        _ui.update { it.copy(batchId = id) }
    }

    fun save() = viewModelScope.launch {
        val q = _ui.value.quantity.toDoubleOrNull()
        if (_ui.value.location.isBlank() || q == null || q <= 0.0) {
            _ui.update { it.copy(message = "Enter valid location and quantity") }
            return@launch
        }
        addUseCase(
            HoneyBatch(
                dateEpochMillis = _ui.value.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                location = _ui.value.location.trim(),
                quantityKg = q,
                floralSource = _ui.value.floralSource,
                gradeColor = _ui.value.gradeColor,
                moistureLevel = _ui.value.moistureLevel,
                batchId = _ui.value.batchId
            )
        )
        _ui.update { it.copy(location = "", quantity = "", message = "Saved batch ${it.batchId}") }
        refreshBatchId()
    }
}

@HiltViewModel
class DashboardViewModel @Inject constructor(getDashboardUseCase: GetDashboardUseCase) : ViewModel() {
    val marketPrice = MutableStateFlow(450.0)
    val wholesalePrice = MutableStateFlow(280.0)

    val ui = getDashboardUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        com.jenugumpu.domain.model.DashboardStats()
    )
}

@HiltViewModel
class BatchListViewModel @Inject constructor(
    getBatchesUseCase: GetBatchesUseCase,
    private val deleteUseCase: DeleteHoneyBatchUseCase
) : ViewModel() {
    val batches = getBatchesUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteBatch(batch: HoneyBatch) {
        viewModelScope.launch {
            deleteUseCase(batch)
        }
    }
}

@HiltViewModel
class ProfitViewModel @Inject constructor(
    getBatchesUseCase: GetBatchesUseCase,
    private val calc: CalculateProfitUseCase
) : ViewModel() {
    private val cost = MutableStateFlow("0")
    private val sell = MutableStateFlow("0")
    val batches = getBatchesUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val result = combine(cost, sell, batches) { c, s, all ->
        val costV = c.toDoubleOrNull() ?: 0.0
        val sellV = s.toDoubleOrNull() ?: 0.0
        val total = all.sumOf { calc(it.quantityKg, costV, sellV) }
        Triple(sellV - costV, total, all)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Triple(0.0, 0.0, emptyList<HoneyBatch>()))

    fun onCost(v: String) { cost.value = v }
    fun onSell(v: String) { sell.value = v }

    fun getSuggestion(): String {
        return if (result.value.second > 1000) "Good Profit" else "Improve pricing"
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("jenu_prefs", android.content.Context.MODE_PRIVATE)

    // ── Language ──────────────────────────────────────────────────────────
    private val _isKannada = MutableStateFlow(prefs.getBoolean("isKannada", false))
    val isKannada = _isKannada.asStateFlow()

    // ── Dark Mode ─────────────────────────────────────────────────────────
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("isDarkMode", false))
    val isDarkMode = _isDarkMode.asStateFlow()

    // ── Profile ───────────────────────────────────────────────────────────
    private val _profile = MutableStateFlow(
        UserProfile(
            name           = prefs.getString("name", "") ?: "",
            village        = prefs.getString("village", "") ?: "",
            phone          = prefs.getString("phone", "") ?: "",
            role           = prefs.getString("role", "Honey Producer") ?: "Honey Producer",
            yearsExperience = prefs.getString("yearsExperience", "") ?: "",
            bio            = prefs.getString("bio", "") ?: ""
        )
    )
    val profile = _profile.asStateFlow()

    // ── Save feedback ─────────────────────────────────────────────────────
    private val _profileSaved = MutableStateFlow(false)
    val profileSaved = _profileSaved.asStateFlow()

    // ── Setters ───────────────────────────────────────────────────────────
    fun setKannada(v: Boolean) {
        _isKannada.value = v
        prefs.edit().putBoolean("isKannada", v).apply()
    }

    fun setDarkMode(v: Boolean) {
        _isDarkMode.value = v
        prefs.edit().putBoolean("isDarkMode", v).apply()
    }

    fun setName(v: String)       { _profile.update { it.copy(name = v) } }
    fun setVillage(v: String)    { _profile.update { it.copy(village = v) } }
    fun setPhone(v: String)      { _profile.update { it.copy(phone = v) } }
    fun setRole(v: String)       { _profile.update { it.copy(role = v) } }
    fun setExperience(v: String) { _profile.update { it.copy(yearsExperience = v) } }
    fun setBio(v: String)        { _profile.update { it.copy(bio = v) } }

    /** Persists all profile fields to SharedPreferences and shows a confirmation. */
    fun saveProfile() {
        val p = _profile.value
        prefs.edit()
            .putString("name",            p.name)
            .putString("village",         p.village)
            .putString("phone",           p.phone)
            .putString("role",            p.role)
            .putString("yearsExperience", p.yearsExperience)
            .putString("bio",             p.bio)
            .apply()
        viewModelScope.launch {
            _profileSaved.value = true
            kotlinx.coroutines.delay(2500)
            _profileSaved.value = false
        }
    }
}
