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
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.usecase.CalculateAndSaveUseCase
import org.dishch.medcalculator.domain.usecase.GetDosageRegimensUseCase
import org.dishch.medcalculator.domain.usecase.ValidateInputUseCase
import org.dishch.medcalculator.domain.usecase.ValidationErrorMessagesUseCase
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.PreferencesRepository
import org.dishch.medcalculator.domain.usecase.GetMedicationByIdUseCase
import org.dishch.medcalculator.ui.helpers.isYears
import org.jetbrains.compose.resources.StringResource

data class MainUiState(
    val weight: String = "12.5",
    val age: String = "3",
    val ageUnit: AgeUnit = AgeUnit.YEARS,
    val selectedMedication: Medication? = null,
    val dosageRegimens: List<DosageRegimen> = emptyList(),
    val weightSupportingText: StringResource? = null,
    val ageSupportingText: StringResource? = null,
    val minMonths: Int = 1,
    val minWeight: Double? = 1.0,
    val disableMonths: Boolean = minMonths.isYears()
)

class MainViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val calculateAndSaveUseCase: CalculateAndSaveUseCase,
    private val validateInputUseCase: ValidateInputUseCase,
    private val validationErrorMessagesUseCase: ValidationErrorMessagesUseCase,
    private val getMedicationByIdUseCase: GetMedicationByIdUseCase,
    private val getDosageRegimensUseCase: GetDosageRegimensUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    val isCalculationEnabled: StateFlow<Boolean> = _uiState.map { state ->
        val validationState = validateInputUseCase(
            weightString = state.weight,
            ageString = state.age,
            ageUnit = state.ageUnit,
            minMonths = state.minMonths,
            minWeight = state.minWeight
        )
        validationState.isValid && state.selectedMedication != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        viewModelScope.launch {
            preferencesRepository.weight.collectLatest { weight ->
                _uiState.update { it.copy(weight = weight.toString()) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.age.collectLatest { age ->
                _uiState.update { it.copy(age = age.toString()) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.ageUnit.collectLatest { ageUnit ->
                _uiState.update { it.copy(ageUnit = ageUnit) }
            }
        }
        viewModelScope.launch {
            preferencesRepository.medicationId.collectLatest { id ->
                getMedicationByIdUseCase(id).collect { medication ->
                    _uiState.update { it.copy(selectedMedication = medication) }
                }
            }
        }

        viewModelScope.launch {
            _uiState.collectLatest { state ->
                state.selectedMedication?.let { medication ->
                    getDosageRegimensUseCase(medication.id).collect { regimens ->
                        _uiState.update {
                            it.copy(
                                dosageRegimens = regimens,
                                minMonths = state.selectedMedication.ageLimit,
                                minWeight = regimens.mapNotNull { it.fromKg }.minOrNull(),
                                disableMonths = state.selectedMedication.ageLimit.isYears()
                            )
                        }
                    }
                }
            }
        }

        // Validate input on state changes
        viewModelScope.launch {
            _uiState.collect { state ->
                val validationState = validateInputUseCase(
                    weightString = state.weight,
                    ageString = state.age,
                    ageUnit = state.ageUnit,
                    minMonths = state.minMonths,
                    minWeight = state.minWeight
                )
                val errorMessages = validationErrorMessagesUseCase(
                    validationState = validationState,
                    ageUnit = state.ageUnit
                )
                _uiState.update {
                    it.copy(
                        ageUnit = if (state.disableMonths) AgeUnit.YEARS else state.ageUnit,
                        weightSupportingText = errorMessages.weightSupportingText,
                        ageSupportingText = errorMessages.ageSupportingText
                    )
                }
            }
        }
    }

    suspend fun calculate(): CalculationResults? {
        val state = _uiState.value
        val weight = state.weight.toDoubleOrNull() ?: return null
        val age = state.age.toIntOrNull() ?: return null
        
        return calculateAndSaveUseCase(
            weight = weight,
            age = age,
            ageUnit = state.ageUnit,
            medication = state.selectedMedication,
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
