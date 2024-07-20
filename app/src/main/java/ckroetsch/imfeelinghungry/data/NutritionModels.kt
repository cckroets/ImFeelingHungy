package ckroetsch.imfeelinghungry.data

enum class NutritionUnit(val suffix: String) {
    CALORIES(""),
    GRAMS("g"),
    MILLIGRAMS("mg"),
}

enum class DailyValue(val value: Int, val unit: NutritionUnit) {
    CALORIES(2000, NutritionUnit.CALORIES),
    CARBOHYDRATES(275, NutritionUnit.GRAMS),
    FAT(78, NutritionUnit.GRAMS),
    PROTEIN(50, NutritionUnit.GRAMS),
    FIBER(28, NutritionUnit.GRAMS),
    SODIUM(2300, NutritionUnit.MILLIGRAMS),
    ADDED_SUGAR(50, NutritionUnit.GRAMS),
    SATURATED_FAT(20, NutritionUnit.GRAMS),
    CHOLESTEROL(300, NutritionUnit.MILLIGRAMS),
}
