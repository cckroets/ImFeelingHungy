// DataStoreUtils.kt
package ckroetsch.imfeelinghungry

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import ckroetsch.imfeelinghungry.data.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun savePreferences(dataStore: DataStore<Preferences>, key: Preferences.Key<String>, data: Map<String, Preference>) {
    val serializedData = data.map { "${it.key}:${it.value.name}" }.joinToString(",")
    dataStore.edit { preferences ->
        preferences[key] = serializedData
    }
}

suspend fun isDataStoreEmpty(dataStore: DataStore<Preferences>): Boolean {
    val preferences = dataStore.data.map { it }.first()
    return preferences == emptyPreferences()
}

fun getPreferences(dataStore: DataStore<Preferences>, key: Preferences.Key<String>): Flow<Map<String, Preference>> {
    return dataStore.data
        .map { preferences ->
            preferences[key]?.split(",")?.map {
                val (name, preference) = it.split(":")
                name to Preference.valueOf(preference)
            }?.toMap() ?: emptyMap()
        }
}
