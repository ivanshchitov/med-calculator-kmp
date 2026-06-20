package org.dishch.medcalculator.domain

import kotlinx.serialization.Serializable

@Serializable
data class MedicationUi(
    val name: String,
    val dose: String
)
