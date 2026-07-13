package org.dishch.medcalculator.data.local

import org.dishch.medcalculator.domain.model.Route as DomainRoute
import org.dishch.medcalculator.domain.model.DosageUnit as DomainDosageUnit

fun Route.toDomain(): DomainRoute = when (this) {
    Route.IV -> DomainRoute.IV
    Route.IM -> DomainRoute.IM
    Route.SC -> DomainRoute.SC
}

fun DomainRoute.toData(): Route = when (this) {
    DomainRoute.IV -> Route.IV
    DomainRoute.IM -> Route.IM
    DomainRoute.SC -> Route.SC
}

fun DosageUnit.toDomain(): DomainDosageUnit = when (this) {
    DosageUnit.MG -> DomainDosageUnit.MG
    DosageUnit.MG_PER_KG -> DomainDosageUnit.MG_PER_KG
}

fun DomainDosageUnit.toData(): DosageUnit = when (this) {
    DomainDosageUnit.MG -> DosageUnit.MG
    DomainDosageUnit.MG_PER_KG -> DosageUnit.MG_PER_KG
}
