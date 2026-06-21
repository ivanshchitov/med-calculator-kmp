package org.dishch.medcalculator.domain

import kotlinx.serialization.Serializable

@Serializable
data class Medication(
    val id: Long,
    val name: String,
    val dosage: Int,
    val maxSingleDose: Int,
    val ageLimit: Int
)
