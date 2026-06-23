package org.dishch.medcalculator.domain

data class DosageRegimen(
    val id: Long,
    val fromAge: Int,
    val toAge: Int,
    val minDosePerKg: Double,
    val maxDosePerKg: Double,
    val medicationId: Long
)

val DosageRegimen.formattedMinDose: String
    get() = if (minDosePerKg % 1.0 == 0.0) {
        minDosePerKg.toInt().toString()
    } else {
        minDosePerKg.toString()
    }

val DosageRegimen.formattedMaxDose: String
    get() = if (maxDosePerKg % 1.0 == 0.0) {
        maxDosePerKg.toInt().toString()
    } else {
        maxDosePerKg.toString()
    }

val DosageRegimen.formattedDoseRange: String
    get() = "$formattedMinDose-$formattedMaxDose"