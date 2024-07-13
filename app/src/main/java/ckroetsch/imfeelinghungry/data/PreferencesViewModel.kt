package ckroetsch.imfeelinghungry.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ckroetsch.imfeelinghungry.DIETARY_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.FOOD_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.RESTAURANT_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.dataStore
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import androidx.compose.runtime.mutableStateMapOf
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import ckroetsch.imfeelinghungry.savePreferences
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}

private fun String.toPrefsMap(): Map<String, Preference> {
    return this.split(",").associate {
        val (name, preference) = it.split(":")
        name to Preference.valueOf(preference)
    }
}


class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    // TODO: Store this data in DataStore
    val dietaryPreferences = mutableStateMapOf<String, Preference>()
    val foodPreferences = mutableStateMapOf<String, Preference>()
    val restaurantPreferences = mutableStateMapOf<String, Preference>()

    init {
        viewModelScope.launch {
            dataStore.data.onEach { preferences ->
                preferences[DIETARY_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    dietaryPreferences.putAll(it)
                }
                preferences[FOOD_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    foodPreferences.putAll(it)
                }
                preferences[RESTAURANT_PREFERENCES_KEY]?.toPrefsMap()?.let {
                    restaurantPreferences.putAll(it)
                }
            }.collect()
        }
    }

    fun setDietaryPreference(diet: Diet, preference: Preference) {
        dietaryPreferences[diet.name] = preference
        viewModelScope.launch {
            savePreferences(dataStore, DIETARY_PREFERENCES_KEY, dietaryPreferences)
        }
    }

    fun checkIfDataStoreIsEmpty(): Boolean = runBlocking {
        isDataStoreEmpty(dataStore)
    }

    fun setFoodPreference(food: Food, preference: Preference) {
        foodPreferences[food.name] = preference
        viewModelScope.launch {
            savePreferences(dataStore, FOOD_PREFERENCES_KEY, foodPreferences)
        }
    }

    fun setRestaurantPreference(restaurant: Restaurant, preference: Preference) {
        restaurantPreferences[restaurant.name] = preference
        viewModelScope.launch {
            savePreferences(dataStore, RESTAURANT_PREFERENCES_KEY, restaurantPreferences)
        }
    }

    suspend fun isDataStoreEmpty(): Boolean {
        return isDataStoreEmpty(dataStore)
    }
}
