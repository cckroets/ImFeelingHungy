package ckroetsch.imfeelinghungry.data

enum class NutritionUnit(val suffix: String) {
    CALORIES(""),
    GRAMS("g"),
    MILLIGRAMS("mg"),
}

enum class NutritionMetric(
    val displayName: String,
    val dv: Int?,
    val unit: NutritionUnit
) {
    CALORIES("Calories", 2000, NutritionUnit.CALORIES),
    CARBOHYDRATES("Carbs", 275, NutritionUnit.GRAMS),
    FAT("Fat", 78, NutritionUnit.GRAMS),
    TRANS_FAT("Trans Fat", null, NutritionUnit.GRAMS),
    PROTEIN("Protein", 50, NutritionUnit.GRAMS),
    FIBER("Fiber", 28, NutritionUnit.GRAMS),
    IRON("Iron", 18, NutritionUnit.MILLIGRAMS),
    SODIUM("Sodium", 2300, NutritionUnit.MILLIGRAMS),
    TOTAL_SUGAR("Sugar", null, NutritionUnit.GRAMS),
    ADDED_SUGAR("Added Sugar", 50, NutritionUnit.GRAMS),
    SATURATED_FAT("Sat. Fat", 20, NutritionUnit.GRAMS),
    CHOLESTEROL("Cholesterol", 300, NutritionUnit.MILLIGRAMS),
}
