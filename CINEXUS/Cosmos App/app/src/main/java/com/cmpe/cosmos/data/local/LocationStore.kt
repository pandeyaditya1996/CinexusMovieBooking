package com.cmpe.cosmos.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("location")
        private val LOCATION_KEY = stringPreferencesKey("location_key")
    }

    val getLocation: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LOCATION_KEY] ?: ""
    }

    suspend fun saveLocation(token: String) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_KEY] = token
        }
    }
}