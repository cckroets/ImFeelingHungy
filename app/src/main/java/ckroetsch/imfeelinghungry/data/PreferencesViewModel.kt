package ckroetsch.imfeelinghungry.data

import androidx.lifecycle.ViewModel
import ckroetsch.imfeelinghungry.onboarding.Diet
import ckroetsch.imfeelinghungry.onboarding.Food
import ckroetsch.imfeelinghungry.onboarding.Restaurant

enum class Preference {
    LIKED,
    DISLIKED,
    DEFAULT
}


class PreferencesViewModel : ViewModel() {

    // TODO: Store this data in DataStore
    val dietaryPreferences = mutableMapOf<String, Preference>()
    val foodPreferences = mutableMapOf<String, Preference>()
    val restaurantPreferences = mutableMapOf<String, Preference>()

    fun setDietaryPreference(diet: Diet, preference: Preference) {
        dietaryPreferences[diet.name] = preference
    }

    fun setFoodPreference(food: Food, preference: Preference) {
        foodPreferences[food.name] = preference
    }

    fun setRestaurantPreference(restaurant: Restaurant, preference: Preference) {
        restaurantPreferences[restaurant.name] = preference
    }

    fun isDietaryPreferencePreferred(diet: Diet): Boolean {
        return dietaryPreferences[diet.name] == Preference.LIKED
    }


}
