package ckroetsch.imfeelinghungry.data

import androidx.compose.runtime.mutableStateMapOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ckroetsch.imfeelinghungry.DIETARY_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.FOOD_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.RESTAURANT_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import ckroetsch.imfeelinghungry.savePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserPreferences(
    private val scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>
) {
    private val _dietaryPreferences = mutableStateMapOf<String, Preference>()
    private val _foodPreferences = mutableStateMapOf<String, Preference>()
    private val _restaurantPreferences = mutableStateMapOf<String, Preference>()

    val dietaryPreferences: Map<String, Preference> get() = _dietaryPreferences
    val foodPreferences: Map<String, Preference> get() = _foodPreferences
    val restaurantPreferences: Map<String, Preference> get() = _restaurantPreferences

    fun setDietaryPreference(diet: Diet, preference: Preference) {
        _dietaryPreferences[diet.name] = preference
        scope.launch {
            savePreferences(dataStore, DIETARY_PREFERENCES_KEY, _dietaryPreferences)
        }
    }


    fun checkIfDataStoreIsEmpty(): Boolean = runBlocking {
        isDataStoreEmpty(dataStore)
    }

    fun setFoodPreference(food: Food, preference: Preference) {
        _foodPreferences[food.name] = preference
        scope.launch {
            savePreferences(dataStore, FOOD_PREFERENCES_KEY, _foodPreferences)
        }
    }

    fun setRestaurantPreference(restaurant: Restaurant, preference: Preference) {
        _restaurantPreferences[restaurant.name] = preference
        scope.launch {
            savePreferences(dataStore, RESTAURANT_PREFERENCES_KEY, _restaurantPreferences)
        }
    }

    fun observeChanges() {
        scope.launch {
            dataStore.data.onEach { preferences ->
                preferences[DIETARY_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    _dietaryPreferences.putAll(it)
                }
                preferences[FOOD_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    _foodPreferences.putAll(it)
                }
                preferences[RESTAURANT_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    _restaurantPreferences.putAll(it)
                }
            }.collect()
        }
    }


}



private fun String.toPrefsMap(): Map<String, Preference> {
    return this.split(",").associate {
        val (name, preference) = it.split(":")
        name to Preference.valueOf(preference)
    }
}

