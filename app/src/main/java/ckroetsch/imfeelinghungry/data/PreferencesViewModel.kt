package ckroetsch.imfeelinghungry.data

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ckroetsch.imfeelinghungry.R
import ckroetsch.imfeelinghungry.dataStore
import ckroetsch.imfeelinghungry.isDataStoreEmpty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString

enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}

class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = application.dataStore
    val preferences = UserPreferences(viewModelScope, dataStore)
    private val chef = GenerativeChef(application, application.assets, preferences)
    private val _favorites = MutableStateFlow<List<MenuItem>>(emptyList())
    val favorites: StateFlow<List<MenuItem>> get() = _favorites

    private val item: MutableStateFlow<Result<MenuItem>> = MutableStateFlow(Result.None)

    val generatedMenuItem: StateFlow<Result<MenuItem>> get() = item

    init {
        preferences.observeChanges()
        loadFavorites() // Load favorites from data store or other persistent storage

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
        this.item.value = Result.Loading
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

    fun lookupRestuarantImage(restaurantName: String): Int {
        AllRestaurants.forEach {
            if (it.name == restaurantName) {
                return it.imageUrl
            }
        }
        // Return a default image resource ID if the restaurant is not found
        return R.drawable._004_fish // Replace with your default image resource
    }

    // Functions to manage the favorites list
    fun addFavorite(menuItem: MenuItem) {
        _favorites.update { currentFavorites ->
            currentFavorites + menuItem
        }
        saveFavorites() // Save the updated favorites list
    }

    fun removeFavorite(menuItem: MenuItem) {
        _favorites.update { currentFavorites ->
            currentFavorites.filterNot { it == menuItem }
        }
        saveFavorites() // Save the updated favorites list
    }

    fun isFavorite(menuItem: MenuItem): Boolean {
        return _favorites.value.contains(menuItem)
    }

    // Functions to load and save favorites to/from persistent storage
    private fun loadFavorites() {
        viewModelScope.launch {
            val favoritesJson = dataStore.data.map { preferences ->
                preferences[FAVORITES_KEY] ?: "[]"
            }.first()

            _favorites.value = HungryJson.decodeFromString(favoritesJson)
        }
    }

    private fun saveFavorites() {
        viewModelScope.launch {
            val favoritesJson = HungryJson.encodeToString(_favorites.value)
            dataStore.edit { preferences ->
                preferences[FAVORITES_KEY] = favoritesJson
            }
        }
    }

    companion object {
        private val FAVORITES_KEY = stringPreferencesKey("favorites")
    }
}
