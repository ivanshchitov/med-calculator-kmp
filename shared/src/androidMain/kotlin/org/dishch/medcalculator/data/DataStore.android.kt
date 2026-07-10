package org.dishch.medcalculator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun dataStorePath(context: Context): String =
    context.filesDir.resolve(dataStoreFileName).absolutePath

fun createDataStore(context: Context): DataStore<Preferences> = 
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { dataStorePath(context).toPath() }
    )

// Keep the expect/actual structure as requested, 
// though it's technically not needed if we inject via Koin
actual fun dataStorePath(): String =
    throw UnsupportedOperationException("Use dataStorePath(context: Context)")
