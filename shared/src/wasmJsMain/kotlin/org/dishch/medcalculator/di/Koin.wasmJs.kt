package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.createDataStore
import org.dishch.medcalculator.data.repository.WebMedicationRepository
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.koin.core.Koin
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun platformModule() = module {
    single { createDataStore() }
    single { WebMedicationRepository(get()) } bind MedicationRepository::class
}

actual suspend fun initializePlatformData(koin: Koin) = Unit
