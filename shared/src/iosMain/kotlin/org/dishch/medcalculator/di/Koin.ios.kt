package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.createDataStore
import org.dishch.medcalculator.data.local.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getDatabaseBuilder() }
    single { createDataStore() }
}
