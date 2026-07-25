package org.dishch.medcalculator.domain.model

import kotlinx.serialization.Serializable
import org.dishch.medcalculator.formatAsDecimal

@Serializable
data class Medication(
    val id: String,
    val name: String,
    val dosage: Double,
    val maxSingleDose: Double?,
    val ageLimit: Int
)

val Medication.formattedDosage: String
    get() = dosage.formatAsDecimal(3)

val Medication.formattedMaxSingleDose: String
    get() = maxSingleDose?.formatAsDecimal(3) ?: ""
