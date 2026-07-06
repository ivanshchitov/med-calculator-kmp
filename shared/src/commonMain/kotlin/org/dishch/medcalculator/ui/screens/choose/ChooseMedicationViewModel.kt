package org.dishch.medcalculator.ui.screens.choose

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationRepository

class ChooseMedicationViewModel(
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMedication = MutableStateFlow<Medication?>(null)
    val selectedMedication: StateFlow<Medication?> = _selectedMedication.asStateFlow()

    private val _selectedMedicationColor = MutableStateFlow<Color>(Color.Transparent)
    val selectedMedicationColor: StateFlow<Color> = _selectedMedicationColor.asStateFlow()

    private val _regimens = MutableStateFlow<List<DosageRegimen>>(emptyList())
    val regimens: StateFlow<List<DosageRegimen>> = _regimens.asStateFlow()

    private val _showInfo = MutableStateFlow(false)
    val showInfo: StateFlow<Boolean> = _showInfo.asStateFlow()

    val medications: StateFlow<List<Medication>> = medicationRepository.getAllMedications()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredMedications: StateFlow<List<Medication>> = combine(
        medications,
        _searchQuery
    ) { medications, query ->
            medications.filter { it.name.startsWith(query, ignoreCase = true) }
    }.flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onInfoClick(medication: Medication, color: Color) {
        viewModelScope.launch {
            _selectedMedication.value = medication
            _selectedMedicationColor.value = color
            _regimens.value = medicationRepository.getRegimensForMedication(medication.id).first()
            _showInfo.value = true
        }
    }

    fun onDismissInfo() {
        _showInfo.value = false
        _selectedMedication.value = null
        _regimens.value = emptyList()
    }
}
