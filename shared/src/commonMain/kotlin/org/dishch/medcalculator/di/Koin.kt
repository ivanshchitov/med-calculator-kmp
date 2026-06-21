package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.local.AppDatabase
import org.dishch.medcalculator.data.local.getRoomDatabase
import org.dishch.medcalculator.data.local.initializeDatabase
import org.dishch.medcalculator.data.repository.MedicationRepositoryImpl
import org.dishch.medcalculator.domain.MedicationRepository
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            repositoryModule,
            viewModelModule,
            platformModule()
        )
    }.koin.let { koin ->
        val database = koin.get<AppDatabase>()
        MainScope().launch {
            initializeDatabase(database)
        }
    }

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().getMedicationDao() }
    single { get<AppDatabase>().getDosageRegimenDao() }
}

val repositoryModule = module {
    singleOf(::MedicationRepositoryImpl) bind MedicationRepository::class
}

val viewModelModule = module {
    factoryOf(::ChooseMedicationViewModel)
}

expect fun platformModule(): Module
