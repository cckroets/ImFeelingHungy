package ckroetsch.imfeelinghungry.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ckroetsch.imfeelinghungry.dataStore
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.DietType
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.NutritionGoal
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking


enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}


class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore
    val preferences = UserPreferences(viewModelScope, dataStore)
    private val chef = GenerativeChef(application, application.assets, preferences)

    private val item: MutableStateFlow<Result<MenuItem>> = MutableStateFlow(Result.Loading)

    val generatedMenuItem: StateFlow<Result<MenuItem>> get() = item

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

    fun generateMenuItem() {
        chef.generateFromChat().onEach { item ->
            this.item.value = item
        }.launchIn(viewModelScope)
    }

    fun regenerateWithInstructions(instructions: String) {
        chef.regenerateWithInstructions(instructions).onEach { item ->
            this.item.value = item
        }.launchIn(viewModelScope)
    }

    val dietGoals: List<NutritionGoal> get() = preferences.dietaryPreferences
        .filter { it.value == Preference.LIKED }
        .mapNotNull { preferredDiet -> DietType.entries.firstOrNull { preferredDiet.key == it.idName } }
        .flatMap { it.goals }

    suspend fun isDataStoreEmpty(): Boolean {
        return isDataStoreEmpty(dataStore)
    }
}
