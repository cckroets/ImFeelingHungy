package ckroetsch.imfeelinghungry.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ckroetsch.imfeelinghungry.dataStore
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}


class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore
    val preferences = UserPreferences(viewModelScope, dataStore)
    private val chef = GenerativeChef(application.assets, preferences)

    private val item by lazy {
        chef.generateMenuItem()
    }

    init {
        preferences.observeChanges()
    }

    fun setDietaryPreference(diet: Diet, preference: Preference) {
        preferences.setDietaryPreference(diet, preference)
    }

    fun checkIfDataStoreIsEmpty(): Boolean = runBlocking {
        isDataStoreEmpty(dataStore)
    }

    fun setFoodPreference(food: Food, preference: Preference) {
        preferences.setFoodPreference(food, preference)
    }

    fun setRestaurantPreference(restaurant: Restaurant, preference: Preference) {
        preferences.setRestaurantPreference(restaurant, preference)
    }

    fun generateMenuItem(): Flow<Result<MenuItem>> = item

    suspend fun isDataStoreEmpty(): Boolean {
        return isDataStoreEmpty(dataStore)
    }
}
