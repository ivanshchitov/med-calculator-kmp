package org.dishch.medcalculator.ui.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.dishch.medcalculator.domain.AgeUnit

class MainViewModel : ViewModel() {

    private val _weight = MutableStateFlow("12.5")
    val weight: StateFlow<String> = _weight

    private val _age = MutableStateFlow("3")
    val age: StateFlow<String> = _age

    private val _ageUnit = MutableStateFlow(AgeUnit.YEARS)
    val ageUnit: StateFlow<AgeUnit> = _ageUnit

    fun onWeightChanged(newWeight: String) {
        _weight.value = newWeight
    }

    fun onAgeChanged(newAge: String) {
        _age.value = newAge
    }

    fun onAgeUnitChanged(newUnit: AgeUnit) {
        _ageUnit.value = newUnit
    }
}
