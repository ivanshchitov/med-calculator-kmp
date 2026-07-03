package org.dishch.medcalculator.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.usecase.SaveStateUseCase
import org.dishch.medcalculator.domain.usecase.CalculationUseCase
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.dishch.medcalculator.ui.InputValidator

data class MainUiState(
    val weight: String = "12.5",
    val age: String = "3",
    val ageUnit: AgeUnit = AgeUnit.YEARS,
    val selectedMedication: Medication? = null,
    val dosageRegimens: List<DosageRegimen> = emptyList()
)

class MainViewModel(
    private val medicationRepository: MedicationRepository,
    private val calculationUseCase: CalculationUseCase,
    private val saveStateUseCase: SaveStateUseCase,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val isCalculationEnabled: StateFlow<Boolean> = _uiState.map { state ->
        InputValidator.validateWeight(state.weight)
                && InputValidator.validateAge(state.age, state.ageUnit)
                && state.selectedMedication != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        viewModelScope.launch {
            preferenceManager.weight.collectLatest { weight ->
                _uiState.update { it.copy(weight = weight.toString()) }
            }
        }
        viewModelScope.launch {
            preferenceManager.age.collectLatest { age ->
                _uiState.update { it.copy(age = age.toString()) }
            }
        }
        viewModelScope.launch {
            preferenceManager.ageUnit.collectLatest { ageUnit ->
                _uiState.update { it.copy(ageUnit = ageUnit) }
            }
        }
        viewModelScope.launch {
            preferenceManager.medicationId.collectLatest { id ->
                medicationRepository.getMedicationById(id).collect { medication ->
                    _uiState.update { it.copy(selectedMedication = medication) }
                }
            }
        }

        viewModelScope.launch {
            _uiState.collectLatest { state ->
                state.selectedMedication?.let { medication ->
                    medicationRepository.getRegimensForMedication(medication.id).collect { regimens ->
                        _uiState.update { it.copy(dosageRegimens = regimens) }
                    }
                }
            }
        }
    }

    fun calculate(): CalculationResults? {
        val state = _uiState.value
        val weight = state.weight.toDoubleOrNull() ?: 0.0
        val age = state.age.toIntOrNull() ?: 0
        
        val result = calculationUseCase(
            weight = weight,
            age = age,
            ageUnit = state.ageUnit,
            selectedMedication = state.selectedMedication,
            dosageRegimens = state.dosageRegimens
        )
        
        // Save state after calculation
        viewModelScope.launch {
            result?.let {
                saveStateUseCase(
                    weight = weight,
                    age = age,
                    ageUnit = state.ageUnit,
                    medicationId = state.selectedMedication?.id ?: 1
                )
            }
        }
        
        return result
    }

    fun onWeightChanged(newWeight: String) {
        _uiState.update { it.copy(weight = newWeight) }
    }

    fun onAgeChanged(newAge: String) {
        _uiState.update { it.copy(age = newAge) }
    }

    fun onAgeUnitChanged(newUnit: AgeUnit) {
        _uiState.update { it.copy(ageUnit = newUnit) }
    }

    fun onMedicationChanged(medication: Medication) {
        _uiState.update { it.copy(selectedMedication = medication) }
        // Save medication selection immediately
        viewModelScope.launch {
            saveStateUseCase(
                weight = _uiState.value.weight.toDoubleOrNull() ?: 0.0,
                age = _uiState.value.age.toIntOrNull() ?: 0,
                ageUnit = _uiState.value.ageUnit,
                medicationId = medication.id
            )
        }
    }
}
