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
