package org.dishch.medcalculator.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dishch.medcalculator.domain.AgeUnit
import org.dishch.medcalculator.domain.calculation.CalculationUseCase
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.DosageRegimen
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.MedicationRepository

data class MainUiState(
    val weight: String = "12.5",
    val age: String = "3",
    val ageUnit: AgeUnit = AgeUnit.YEARS,
    val selectedMedication: Medication? = null,
    val dosageRegimens: List<DosageRegimen> = emptyList()
)

class MainViewModel(
    private val medicationRepository: MedicationRepository,
    private val calculationUseCase: CalculationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            medicationRepository.getMedicationById(1).collect { medication ->
                _uiState.update { it.copy(selectedMedication = medication) }
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
        return calculationUseCase(
            weight = state.weight,
            age = state.age,
            ageUnit = state.ageUnit,
            selectedMedication = state.selectedMedication,
            dosageRegimens = state.dosageRegimens
        )
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
    }
}
