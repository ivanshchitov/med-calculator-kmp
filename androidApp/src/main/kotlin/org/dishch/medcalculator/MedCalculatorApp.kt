package org.dishch.medcalculator

import android.app.Application
import com.my.tracker.MyTracker
import com.my.tracker.MyTrackerConfig
import org.dishch.medcalculator.di.initKoin
import org.koin.android.ext.koin.androidContext

class MedCalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initMyTracker()
        initKoin {
            androidContext(this@MedCalculatorApp)
        }
    }

    private fun initMyTracker() {
        val sdkKey = BuildConfig.MYTRACKER_SDK_KEY
        if (sdkKey.isBlank()) return

        MyTracker.setDebugMode(BuildConfig.DEBUG)
        MyTracker.getTrackerConfig()
            .setTrackingLaunchEnabled(true)
            .setLocationTrackingMode(MyTrackerConfig.LocationTrackingMode.NONE)
            .setKidMode(false)
            .setAutotrackingPurchaseEnabled(false)
        MyTracker.initTracker(sdkKey, this)
    }
}
