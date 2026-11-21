package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.data.repository.LocaleRepositoryImpl
import org.dishch.medcalculator.data.repository.PreferencesRepositoryImpl
import org.dishch.medcalculator.domain.repository.LocaleRepository
import org.dishch.medcalculator.domain.repository.PreferencesRepository
import org.dishch.medcalculator.domain.usecase.CalculateAndSaveUseCase
import org.dishch.medcalculator.domain.usecase.CalculationUseCase
import org.dishch.medcalculator.domain.usecase.GetDosageRegimensUseCase
import org.dishch.medcalculator.domain.usecase.GetMedicationByIdUseCase
import org.dishch.medcalculator.domain.usecase.GetMedicationsUseCase
import org.dishch.medcalculator.domain.usecase.SaveStateUseCase
import org.dishch.medcalculator.domain.usecase.ValidateInputUseCase
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationViewModel
import org.dishch.medcalculator.ui.screens.main.MainViewModel
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            preferenceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
            platformModule()
        )
    }.koin.also { koin ->
        MainScope().launch {
            runCatching {
                initializePlatformData(koin)
            }.onFailure { error ->
                println("Platform initialization failed: ${error.stackTraceToString()}")
            }
        }
    }

val repositoryModule = module {
    single<LocaleRepository> { LocaleRepositoryImpl() }
    single {
        PreferencesRepositoryImpl(get())
    } bind PreferencesRepository::class
}

val useCaseModule = module {
    factoryOf(::CalculationUseCase)
    factoryOf(::SaveStateUseCase)
    factoryOf(::CalculateAndSaveUseCase)
    factoryOf(::ValidateInputUseCase)
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

expect suspend fun initializePlatformData(koin: Koin)
