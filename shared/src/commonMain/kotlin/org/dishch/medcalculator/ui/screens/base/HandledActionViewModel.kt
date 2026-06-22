package org.dishch.medcalculator.ui.screens.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class HandledActionViewModel : ViewModel() {

    private val _isActionHandled = MutableStateFlow(false)
    val isActionHandled: StateFlow<Boolean> = _isActionHandled

    fun markActionHandled() {
        _isActionHandled.value = true
    }
}
