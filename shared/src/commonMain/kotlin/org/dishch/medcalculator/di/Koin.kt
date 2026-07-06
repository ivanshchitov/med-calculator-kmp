package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.local.AppDatabase
import org.dishch.medcalculator.data.local.DosageRegimenDao
import org.dishch.medcalculator.data.local.MedicationDao
import org.dishch.medcalculator.data.local.getRoomDatabase
import org.dishch.medcalculator.data.local.initializeDatabase
import org.dishch.medcalculator.data.repository.MedicationRepositoryImpl
import org.dishch.medcalculator.domain.usecase.SaveStateUseCase
import org.dishch.medcalculator.domain.usecase.CalculationUseCase
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationViewModel
import org.dishch.medcalculator.ui.screens.main.MainViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.data.createDataStore
import org.dishch.medcalculator.domain.usecase.ValidateInputUseCase
import org.dishch.medcalculator.domain.usecase.ValidationErrorMessagesUseCase
import org.koin.core.module.dsl.viewModelOf

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
            dataStoreModule,
            preferenceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
            platformModule()
        )
    }.koin.let { koin ->
        val database = koin.get<AppDatabase>()
        val preferenceManager = koin.get<PreferenceManager>()
        MainScope().launch {
            initializeDatabase(database, preferenceManager)
        }
    }

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().getMedicationDao() }
    single { get<AppDatabase>().getDosageRegimenDao() }
}

val repositoryModule = module {
    single {
        MedicationRepositoryImpl(
            medicationDao = get<MedicationDao>(),
            dosageRegimenDao = get<DosageRegimenDao>()
        )
    } bind MedicationRepository::class
}

val useCaseModule = module {
    factoryOf(::CalculationUseCase)
    factoryOf(::SaveStateUseCase)
    factoryOf(::ValidateInputUseCase)
    factoryOf(::ValidationErrorMessagesUseCase)
}

val viewModelModule = module {
    viewModelOf(::ChooseMedicationViewModel)
    viewModelOf(::MainViewModel)
}

val dataStoreModule = module {
    single { createDataStore() }
}

val preferenceModule = module {
    single { PreferenceManager(get()) }
}

expect fun platformModule(): Module
