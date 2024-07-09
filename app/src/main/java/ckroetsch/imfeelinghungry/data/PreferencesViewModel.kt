package ckroetsch.imfeelinghungry.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ckroetsch.imfeelinghungry.DIETARY_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.FOOD_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.RESTAURANT_PREFERENCES_KEY
import ckroetsch.imfeelinghungry.dataStore
import ckroetsch.imfeelinghungry.getPreferences
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import ckroetsch.imfeelinghungry.savePreferences
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}


class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore

    // TODO: Store this data in DataStore
    val dietaryPreferences = mutableMapOf<String, Preference>()
    val foodPreferences = mutableMapOf<String, Preference>()
    val restaurantPreferences = mutableMapOf<String, Preference>()

    init {
        viewModelScope.launch {
            getPreferences(dataStore, DIETARY_PREFERENCES_KEY).collect {
                dietaryPreferences.putAll(it)
            }
            getPreferences(dataStore, FOOD_PREFERENCES_KEY).collect {
                foodPreferences.putAll(it)
            }
            getPreferences(dataStore, RESTAURANT_PREFERENCES_KEY).collect {
                restaurantPreferences.putAll(it)
            }
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
