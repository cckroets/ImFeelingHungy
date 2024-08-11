package ckroetsch.imfeelinghungry.data

import kotlinx.serialization.Serializable

// Top level data class
@Serializable
data class MenuItem(
    val restaurantName: String,
    val creationTitle: String?,
    val creationDescription: String,
    val menuItemTitle: String,
    val reasons: List<Reason>,
    val originalNutrition: NutritionalInformation,
    val appliedCustomizations: List<MenuCustomization>,
    val redoModifiers: List<String>
)

// Add 2 NutritionalInformation objects
fun NutritionalInformation.add(other: NutritionalInformation): NutritionalInformation {
    return NutritionalInformation(
        servingSize = this.servingSize,
        calories = this.calories?.plus(other.calories ?: 0),
        carbohydrateContent = this.carbohydrateContent?.plus(other.carbohydrateContent ?: 0.0),
        cholesterolContent = this.cholesterolContent?.plus(other.cholesterolContent ?: 0.0),
        fatContent = this.fatContent?.plus(other.fatContent ?: 0.0),
        fiberContent = this.fiberContent?.plus(other.fiberContent ?: 0.0),
        proteinContent = this.proteinContent?.plus(other.proteinContent ?: 0.0),
        saturatedFatContent = this.saturatedFatContent?.plus(other.saturatedFatContent ?: 0.0),
        sodiumContent = this.sodiumContent?.plus(other.sodiumContent ?: 0.0),
        ironContent = this.ironContent?.plus(other.ironContent ?: 0.0),
        sugarContent = this.sugarContent?.plus(other.sugarContent ?: 0.0),
        transFatContent = this.transFatContent?.plus(other.transFatContent ?: 0.0)
    )
}

fun MenuItem.calculateFinalNutrition(): NutritionalInformation {
    return appliedCustomizations
        .mapNotNull { it.nutritionDifference }
        .fold(originalNutrition) { acc, nutritionalInformation -> acc.add(nutritionalInformation) }
}

// Reason nested data class
@Serializable
data class Reason(
    val title: String,
    val description: String
)

// NutritionalInformation nested data class
@Serializable
data class NutritionalInformation(
    val servingSize: String?,
    val calories: Int?,
    val carbohydrateContent: Double?,
    val cholesterolContent: Double?,
    val fatContent: Double?,
    val fiberContent: Double?,
    val proteinContent: Double?,
    val saturatedFatContent: Double?,
    val sodiumContent: Double?,
    val ironContent: Double?,
    val sugarContent: Double?,
    val transFatContent: Double?
)

// MenuCustomization nested data class
@Serializable
data class MenuCustomization(
    val type: ModificationType,
    val ingredient: String,
    val reason: String?,
    val substituted: String?,
    val nutritionDifference: NutritionalInformation?
)

enum class ModificationType {
    Choose,
    Add,
    Remove,
    Substitute
}
