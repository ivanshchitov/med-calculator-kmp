package org.dishch.medcalculator.data.local

import org.dishch.medcalculator.domain.model.Route as DomainRoute
import org.dishch.medcalculator.domain.model.Unit as DomainUnit

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

fun Unit.toDomain(): DomainUnit = when (this) {
    Unit.MG -> DomainUnit.MG
    Unit.MG_PER_KG -> DomainUnit.MG_PER_KG
}

fun DomainUnit.toData(): Unit = when (this) {
    DomainUnit.MG -> Unit.MG
    DomainUnit.MG_PER_KG -> Unit.MG_PER_KG
}
