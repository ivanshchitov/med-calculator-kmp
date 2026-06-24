package org.dishch.medcalculator

fun formattedDouble(value: Double) =
    if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
