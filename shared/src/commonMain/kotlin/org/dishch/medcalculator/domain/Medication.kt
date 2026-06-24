package org.dishch.medcalculator.domain

import kotlinx.serialization.Serializable
import org.dishch.medcalculator.formattedDouble

@Serializable
data class Medication(
    val id: Long,
    val name: String,
    val dosage: Double,
    val maxSingleDose: Double,
    val ageLimit: Int
)

val Medication.formattedDosage: String
    get() = formattedDouble(dosage)

val Medication.formattedMaxSingleDose: String
    get() = formattedDouble(maxSingleDose)
