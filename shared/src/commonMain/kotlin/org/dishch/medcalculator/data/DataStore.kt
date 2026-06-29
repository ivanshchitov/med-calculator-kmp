package org.dishch.medcalculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createDataStore(): DataStore<Preferences>

internal const val dataStoreFileName = "med_calculator.preferences_pb"
