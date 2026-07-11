package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.local.AppDatabase
import org.dishch.medcalculator.data.local.DosageRegimenDao
import org.dishch.medcalculator.data.local.MedicationDao
import org.dishch.medcalculator.data.local.MedicationFullDao
import org.dishch.medcalculator.data.local.getRoomDatabase
import org.dishch.medcalculator.data.local.initializeDatabase
import org.dishch.medcalculator.data.repository.MedicationFullRepositoryImpl
import org.dishch.medcalculator.data.repository.MedicationRepositoryImpl
import org.dishch.medcalculator.data.repository.PreferencesRepositoryImpl
import org.dishch.medcalculator.domain.usecase.SaveStateUseCase
import org.dishch.medcalculator.domain.usecase.CalculationUseCase
import org.dishch.medcalculator.domain.usecase.CalculateAndSaveUseCase
import org.dishch.medcalculator.domain.usecase.GetDosageRegimensUseCase
import org.dishch.medcalculator.domain.usecase.GetMedicationsUseCase
import org.dishch.medcalculator.domain.repository.MedicationFullRepository
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.dishch.medcalculator.domain.repository.PreferencesRepository
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
import org.dishch.medcalculator.domain.usecase.GetMedicationByIdUseCase
import org.dishch.medcalculator.domain.usecase.ValidateInputUseCase
import org.dishch.medcalculator.domain.usecase.ValidationErrorMessagesUseCase
import org.koin.core.module.dsl.viewModelOf

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            databaseModule,
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
    single { get<AppDatabase>().getMedicationFullDao() }
}

val repositoryModule = module {
    single {
        MedicationRepositoryImpl(
            medicationDao = get<MedicationDao>(),
            dosageRegimenDao = get<DosageRegimenDao>()
        )
    } bind MedicationRepository::class
    single {
        MedicationFullRepositoryImpl(
            dao = get<MedicationFullDao>()
        )
    } bind MedicationFullRepository::class
    single {
        PreferencesRepositoryImpl(get())
    } bind PreferencesRepository::class
}

val useCaseModule = module {
    factoryOf(::CalculationUseCase)
    factoryOf(::SaveStateUseCase)
    factoryOf(::CalculateAndSaveUseCase)
    factoryOf(::ValidateInputUseCase)
    factoryOf(::ValidationErrorMessagesUseCase)
    factoryOf(::GetMedicationsUseCase)
    factoryOf(::GetDosageRegimensUseCase)
    factoryOf(::GetMedicationByIdUseCase)
}

val viewModelModule = module {
    viewModelOf(::ChooseMedicationViewModel)
    viewModelOf(::MainViewModel)
}

val preferenceModule = module {
    single { PreferenceManager(get()) }
}

expect fun platformModule(): Module
