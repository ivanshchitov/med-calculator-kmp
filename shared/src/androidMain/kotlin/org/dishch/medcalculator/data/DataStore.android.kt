package org.dishch.medcalculator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

lateinit var appContext: Context

actual fun createDataStore(): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
    produceFile = { appContext.filesDir.resolve(dataStoreFileName).absolutePath.toPath() }
)
