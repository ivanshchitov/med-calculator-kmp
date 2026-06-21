package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.local.AppDatabase
import org.dishch.medcalculator.data.local.getRoomDatabase
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            platformModule()
        )
    }

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().getMedicationDao() }
    single { get<AppDatabase>().getDosageRegimenDao() }
}

expect fun platformModule(): Module
