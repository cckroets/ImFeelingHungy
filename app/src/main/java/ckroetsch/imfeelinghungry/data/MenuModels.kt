package ckroetsch.imfeelinghungry.data

import kotlinx.serialization.Serializable

// Top level data class
@Serializable
data class MenuItem(
    val restaurantName: String,
    val menuItemTitle: String,
    val menuItemLongDescription: String,
    val reasons: List<Reason>,
    val nutrition: NutritionalInformation,
    val appliedCustomizations: List<MenuCustomization>,
    val availableCustomizations: List<MenuCustomization>,
    val redoModifiers: List<String>
)

// Reason nested data class
@Serializable
data class Reason(
    val title: String,
    val description: String
)

// NutritionalInformation nested data class
@Serializable
data class NutritionalInformation(
    val estimated: Boolean,
    val calories: Int?,
    val caloriesFromFat: Int?,
    val carbohydrateContent: Double?,
    val cholesterolContent: Double?,
    val fatContent: Double?,
    val fiberContent: Double?,
    val proteinContent: Double?,
    val saturatedFatContent: Double?,
    val sodiumContent: Double?,
    val sugarContent: Double?,
    val transFatContent: Double?
)

// MenuCustomization nested data class
@Serializable
data class MenuCustomization(
    val type: String,
    val ingredient: String,
    val reason: String?,
    val substituted: String?,
    val nutritionDifference: NutritionalInformation?
)
