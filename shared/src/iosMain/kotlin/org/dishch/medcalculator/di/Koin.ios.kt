package org.dishch.medcalculator.di

import org.dishch.medcalculator.data.createDataStore
import org.koin.dsl.module

actual fun platformModule() = module {
    single { createDataStore() }
}
