package org.dishch.medcalculator.domain.model

data class DosageRegimen(
    val id: Long,
    val fromAge: Int,
    val toAge: Int,
    val minDosePerKg: Double,
    val maxDosePerKg: Double,
    val medicationId: Long
)
