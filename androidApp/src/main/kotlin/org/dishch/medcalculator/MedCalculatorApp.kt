package org.dishch.medcalculator

import android.app.Application
import org.dishch.medcalculator.di.initKoin
import org.koin.android.ext.koin.androidContext

class MedCalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MedCalculatorApp)
        }
    }
}
