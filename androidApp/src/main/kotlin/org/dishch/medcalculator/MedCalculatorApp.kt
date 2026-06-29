package org.dishch.medcalculator

import android.app.Application
import org.dishch.medcalculator.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.dishch.medcalculator.data.appContext

class MedCalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        initKoin {
            androidContext(this@MedCalculatorApp)
        }
    }
}
