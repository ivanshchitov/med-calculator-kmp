package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.createDataStore
import org.dishch.medcalculator.data.local.getDatabaseBuilder
import org.koin.core.Koin
import org.koin.dsl.module

actual fun platformModule() = module {
    includes(medicationRepositoryModule)
    single { getDatabaseBuilder() }
    single { createDataStore() }
}

actual suspend fun initializePlatformData(koin: Koin) {
    initializeNonWebData(koin)
}
