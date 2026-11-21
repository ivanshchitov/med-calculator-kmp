package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.data.local.AppDatabase
import org.dishch.medcalculator.data.local.getRoomDatabase
import org.dishch.medcalculator.data.local.initializeDatabase
import org.dishch.medcalculator.data.repository.MedicationRepositoryImpl
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

val medicationRepositoryModule: Module = module {
    single { getRoomDatabase(get()) }
    single {
        MedicationRepositoryImpl(
            dao = get<AppDatabase>().getMedicationDao(),
            localeRepository = get()
        )
    } bind MedicationRepository::class
}

suspend fun initializeNonWebData(koin: Koin) {
    runCatching {
        initializeDatabase(
            database = koin.get<AppDatabase>(),
            preferenceManager = koin.get<PreferenceManager>()
        ).getOrThrow()
    }.onFailure { error ->
        println("Database initialization failed: ${error.stackTraceToString()}")
    }
}
