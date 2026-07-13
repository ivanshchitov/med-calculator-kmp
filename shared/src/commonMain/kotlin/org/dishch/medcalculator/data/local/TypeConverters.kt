package org.dishch.medcalculator.data.local

import androidx.room.TypeConverter

class RouteTypeConverter {

    @TypeConverter
    fun fromRoute(route: Route): String = route.name

    @TypeConverter
    fun toRoute(value: String): Route = Route.valueOf(value)
}

class UnitTypeConverter {

    @TypeConverter
    fun fromUnit(dosageUnit: DosageUnit): String = dosageUnit.name

    @TypeConverter
    fun toUnit(value: String): DosageUnit = DosageUnit.valueOf(value)
}
