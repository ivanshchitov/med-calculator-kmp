package org.dishch.medcalculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.WebLocalStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer

private const val webDataStoreName = "med_calculator.preferences"

fun createDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        storage = WebLocalStorage(
            serializer = PreferencesSerializer,
            name = webDataStoreName
        )
    )
