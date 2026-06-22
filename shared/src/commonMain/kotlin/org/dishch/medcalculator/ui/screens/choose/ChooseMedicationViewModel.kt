package org.dishch.medcalculator.ui.screens.choose

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.MedicationRepository
import org.dishch.medcalculator.ui.screens.base.HandledActionViewModel

class ChooseMedicationViewModel(
    private val medicationRepository: MedicationRepository
) : HandledActionViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

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
}
