// DataStoreExtensions.kt
package ckroetsch.imfeelinghungry

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val DIETARY_PREFERENCES_KEY = stringPreferencesKey("dietary_preferences")
val FOOD_PREFERENCES_KEY = stringPreferencesKey("food_preferences")
val RESTAURANT_PREFERENCES_KEY = stringPreferencesKey("restaurant_preferences")

